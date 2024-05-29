package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.StockCostAdjustmentDetailDto;
import com.flyemu.share.dto.StockCostAdjustmentDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.form.StockCostAdjustmentForm;
import com.flyemu.share.repository.StockCostAdjustmentDetailRepository;
import com.flyemu.share.repository.StockCostAdjustmentRepository;
import com.flyemu.share.repository.StockItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @功能描述: 成本调整单
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockCostAdjustmentService extends AbsService {
    private final static QUnits qUnits = QUnits.units;
    private final static QAdmin qAdmin = QAdmin.admin;
    private final static QOrder qOrder = QOrder.order;
    private final static QVendors qVendors = QVendors.vendors;
    private final static QStock qStock = QStock.stock;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;
    private final static QStockItem qStockItem = QStockItem.stockItem;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QStockCostAdjustment qStockCostAdjustment = QStockCostAdjustment.stockCostAdjustment;
    private final static QStockCostAdjustmentDetail qStockCostAdjustmentDetail = QStockCostAdjustmentDetail.stockCostAdjustmentDetail;
    private final StockCostAdjustmentDetailRepository detailRepository;
    private final StockCostAdjustmentRepository adjustmentRepository;
    private final StockItemRepository stockItemRepository;
    private final CodeSeedService codeSeedService;


    public PageResults<StockCostAdjustmentDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qStockCostAdjustment)
                .select(qStockCostAdjustment, qWarehouses.name, qProducts.name, qProductsCategory.name, qProducts.code, qAdmin.name)
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStockCostAdjustment.warehouseId))
                .leftJoin(qProducts).on(qProducts.id.eq(qStockCostAdjustment.productsId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .leftJoin(qAdmin).on(qAdmin.id.eq(qStockCostAdjustment.createId))
                .where(query.builder)
                .orderBy(qStockCostAdjustment.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<StockCostAdjustmentDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockCostAdjustmentDto dto = BeanUtil.toBean(tuple.get(qStockCostAdjustment), StockCostAdjustmentDto.class);
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setWarehouseName(tuple.get(qWarehouses.name));
            dto.setCreateName(tuple.get(qAdmin.name));
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }


    @Transactional
    public void save(StockCostAdjustmentForm adjustmentForm, Long adminId, Long merchantId, Long organizationId, String merchantCode) {
        StockCostAdjustment adjustment = new StockCostAdjustment();
        adjustment.setMerchantId(merchantId);
        if (adjustment.getId() != null) {
            throw new ServiceException("怎么会有id了？");
        } else {
            String code = "";
            code = "CBTZB" + merchantCode + codeSeedService.dayIncrease(merchantId, "CBTZB");

            Stock stock = bqf.selectFrom(qStock).where(qStock.id.eq(adjustmentForm.getStockId()).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).fetchFirst();
            BigDecimal cost = stock.getWeightedCost();
            adjustment.setAmount(adjustmentForm.getAmount());
            adjustment.setCode(code);
            adjustment.setCreateId(adminId);
            adjustment.setMerchantId(merchantId);
            adjustment.setOrganizationId(organizationId);
            adjustment.setBillDate(LocalDate.now());
            adjustment.setWarehouseId(adjustmentForm.getWarehouseId());
            adjustment.setProductsId(adjustmentForm.getProductsId());
            adjustmentRepository.save(adjustment);
            Set<Long> sIds = adjustmentForm.getDetailList().stream().map(StockCostAdjustmentDetail::getStockItemId).collect(Collectors.toSet());
            Map<Long, StockItem> itemMap = new HashMap<>();
            bqf.selectFrom(qStockItem)
                    .where(qStockItem.id.in(sIds).and(qStockItem.merchantId.eq(merchantId)).and(qStockItem.organizationId.eq(organizationId))).fetch().forEach(tuple -> {
                        itemMap.put(tuple.getId(), tuple);
                    });
            for (StockCostAdjustmentDetail d : adjustmentForm.getDetailList()) {
                d.setAdjustmentId(adjustment.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
                itemMap.get(d.getStockItemId()).setUnitCost(d.getAfterUnitCost());
                cost = cost.add(NumberUtil.mul(d.getAvailableQuantity(), NumberUtil.sub(d.getAfterUnitCost(), d.getBeforeUnitCost())));
            }
            jqf.update(qStock)
                    .set(qStock.weightedCost, cost)
                    .set(qStock.weightedAverageCost, NumberUtil.div(cost, stock.getTotalQuantity(), 2))
                    .where(qStock.id.eq(adjustmentForm.getStockId()).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).execute();
            stockItemRepository.saveAll(itemMap.values());
            detailRepository.saveAll(adjustmentForm.getDetailList());
        }
    }

    public Dict load(Long adjustmentId, Long merchantId, Long organizationId) {
        Tuple fetchFirst = bqf.selectFrom(qStockCostAdjustment)
                .select(qStockCostAdjustment, qWarehouses.name, qProducts.name, qProducts.code)
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStockCostAdjustment.warehouseId))
                .leftJoin(qProducts).on(qProducts.id.eq(qStockCostAdjustment.productsId))
                .where(qStockCostAdjustment.merchantId.eq(merchantId).and(qStockCostAdjustment.id.eq(adjustmentId)).and(qStockCostAdjustment.organizationId.eq(organizationId))).fetchFirst();

        StockCostAdjustmentDto dto = BeanUtil.toBean(fetchFirst.get(qStockCostAdjustment), StockCostAdjustmentDto.class);
        dto.setProductsName(fetchFirst.get(qProducts.name));
        dto.setProductsCode(fetchFirst.get(qProducts.code));
        dto.setWarehouseName(fetchFirst.get(qWarehouses.name));
        List<StockCostAdjustmentDetailDto> collect = new ArrayList<>();
        jqf.selectFrom(qStockCostAdjustmentDetail)
                .select(qStockCostAdjustmentDetail, qOrder.code, qStockItem.quantity, qVendors.name,qUnits.name)
                .leftJoin(qStockItem).on(qStockCostAdjustmentDetail.stockItemId.eq(qStockItem.id))
                .leftJoin(qOrder).on(qStockItem.orderId.eq(qOrder.id))
                .leftJoin(qVendors).on(qVendors.id.eq(qOrder.vendorsId))
                .leftJoin(qProducts).on(qProducts.id.eq(qStockItem.productsId))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .where(qStockCostAdjustmentDetail.adjustmentId.eq(adjustmentId).and(qStockCostAdjustmentDetail.merchantId.eq(merchantId)).and(qStockCostAdjustmentDetail.organizationId.eq(organizationId)))
                .orderBy(qStockCostAdjustmentDetail.id.asc())
                .fetch().forEach(tuple -> {
                StockCostAdjustmentDetailDto detailDto = BeanUtil.toBean(tuple.get(qStockCostAdjustmentDetail), StockCostAdjustmentDetailDto.class);
                detailDto.setQuantity(tuple.get(qStockItem.quantity));
                detailDto.setOrderCode(tuple.get(qOrder.code));
                detailDto.setVendorsName(tuple.get(qVendors.name));
                detailDto.setUnitName(tuple.get(qUnits.name));
                collect.add(detailDto);
                });
        return Dict.create().set("adjustment", dto).set("detailList", collect);
    }


    @Data
    public static class SelectQuery {
        private LocalDate billDate;
        private Long warehouseId;

        private String filter;
    }

    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();
        public String sortCol;

        public String sort = Constants.SORT_DESC;

        public void setSortCol(String sortCol) {
            this.sortCol = sortCol;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public OrderSpecifier sortSpecifier() {
            if ("createDate".equals(this.sortCol)) {
                if (Constants.SORT_DESC.equals(this.sort)) {
                    return qStockCostAdjustment.createDate.desc();
                } else {
                    return qStockCostAdjustment.createDate.asc();
                }
            } else if ("billDate".equals(this.sortCol)) {
                if (Constants.SORT_DESC.equals(this.sort)) {
                    return qStockCostAdjustment.billDate.desc();
                } else {
                    return qStockCostAdjustment.billDate.asc();
                }
            }
            return qStockCostAdjustment.billDate.desc();
        }

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qStockCostAdjustment.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qStockCostAdjustment.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qStockCostAdjustment.code.contains(filter).or(qProducts.code.contains(filter)).or(qProducts.name.contains(filter)).or(qProducts.pinyin.contains(filter)));
            }
        }

        public void setStart(LocalDate start) {
            if (start != null) {
                builder.and(qStockCostAdjustment.billDate.goe(start));
            }
        }

        public void setEnd(LocalDate end) {
            if (end != null) {
                builder.and(qStockCostAdjustment.billDate.loe(end));
            }
        }
    }
}

