package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.StockInventoryDto;
import com.flyemu.share.dto.StockInventoryItemDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.form.StockInventoryForm;
import com.flyemu.share.repository.StockInventoryItemRepository;
import com.flyemu.share.repository.StockInventoryRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @功能描述: 采购入库单
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockInventoryService extends AbsService {
    private final static QOrder qOutOrder = QOrder.order;
    private final static QOrder qInOrder = new QOrder("id");
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QUnits qUnits = QUnits.units;
    private final static QStock qStock = QStock.stock;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;
    private final static QStockInventory qStockInventory = QStockInventory.stockInventory;
    private final static QStockInventoryItem qStockInventoryItem = QStockInventoryItem.stockInventoryItem;
    private final StockInventoryItemRepository itemRepository;
    private final StockInventoryRepository stockInventoryRepository;
    private final CodeSeedService codeSeedService;


    public PageResults<StockInventoryItemDto> productsSelect(Page page, SelectQuery selectQuery, Long merchantId, Long organizationId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (selectQuery.getWarehouseId() != null) {
            builder.and(qStock.warehouseId.eq(selectQuery.getWarehouseId()));
        }
        if (StrUtil.isNotEmpty(selectQuery.getFilter())) {
            builder.and(qProducts.name.contains(selectQuery.getFilter()).or(qProducts.code.contains(selectQuery.getFilter())));
        }
        PagedList<Tuple> pagedList = bqf.selectFrom(qStock).select(qProducts.unitId,qProducts.id,qProducts.name,qProducts.specification,qProducts.code,qWarehouses.name,qWarehouses.id, qStock.totalQuantity, qUnits.name, qProductsCategory.name)
                .leftJoin(qProducts).on(qProducts.id.eq(qStock.productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId).and(qProductsCategory.merchantId.eq(merchantId)).and(qProductsCategory.organizationId.eq(organizationId)))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId).and(qUnits.merchantId.eq(merchantId)).and(qUnits.organizationId.eq(organizationId)))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStock.warehouseId).and(qWarehouses.merchantId.eq(merchantId)).and(qWarehouses.organizationId.eq(organizationId)))
                .where(builder.and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId)))
                .orderBy(qStock.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<StockInventoryItemDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockInventoryItemDto dto = new StockInventoryItemDto();
            dto.setSysQuantity(tuple.get(qStock.totalQuantity) != null?tuple.get(qStock.totalQuantity):BigDecimal.ZERO);
            dto.setUnitId(tuple.get(qProducts.unitId));
            dto.setUnitName(tuple.get(qUnits.name));
            dto.setWarehouseId(tuple.get(qWarehouses.id));
            dto.setWarehouses(tuple.get(qWarehouses.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setProductsId(tuple.get(qProducts.id));
            dto.setSpecification(tuple.get(qProducts.specification));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }


    public PageResults<StockInventoryDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qStockInventory)
                .select(qStockInventory, qInOrder.code, qOutOrder.code)
                .leftJoin(qInOrder).on(qInOrder.id.eq(qStockInventory.inOrderId))
                .leftJoin(qOutOrder).on(qOutOrder.id.eq(qStockInventory.outOrderId))
                .where(query.builder)
                .orderBy( qStockInventory.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<StockInventoryDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockInventoryDto dto = BeanUtil.toBean(tuple.get(qStockInventory), StockInventoryDto.class);
            dto.setInOrderCode(tuple.get(qInOrder.code));
            dto.setOutOrderCode(tuple.get(qOutOrder.code));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }


    @Transactional
    public void delete(Long inventoryId, Long merchantId, Long organizationId) {
        jqf.delete(qStockInventory).where(qStockInventory.id.eq(inventoryId).and(qStockInventory.merchantId.eq(merchantId)).and(qStockInventory.organizationId.eq(organizationId))).execute();
        jqf.delete(qStockInventoryItem).where(qStockInventoryItem.stockInventoryId.eq(inventoryId).and(qStockInventoryItem.merchantId.eq(merchantId)).and(qStockInventoryItem.organizationId.eq(organizationId))).execute();
    }

    @Transactional
    public void save(StockInventoryForm inventoryForm, Long adminId, Long merchantId, Long organizationId, String merchantCode) {
        StockInventory inventory = inventoryForm.getInventory();
        inventory.setMerchantId(merchantId);
        if (inventory.getId() != null) {
            StockInventory original = stockInventoryRepository.getById(inventory.getId());

            BeanUtil.copyProperties(inventory, original, CopyOptions.create().ignoreNullValue());

            Set<Long> ids = new HashSet<>();
            for (StockInventoryItem d : inventoryForm.getItemList()) {

                if (d.getId() != null) {
                    ids.add(d.getId());
                }
                d.setStockInventoryId(inventory.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            itemRepository.saveAll(inventoryForm.getItemList());
            stockInventoryRepository.save(original);
        } else {
            String code = "";
            code = "PD" + merchantCode + codeSeedService.dayIncrease(inventory.getMerchantId(), "PD");
            inventory.setCode(code);
            inventory.setUserId(adminId);
            inventory.setMerchantId(merchantId);
            inventory.setOrganizationId(organizationId);
            stockInventoryRepository.save(inventory);
            for (StockInventoryItem d : inventoryForm.getItemList()) {
                d.setStockInventoryId(inventory.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            itemRepository.saveAll(inventoryForm.getItemList());
        }
    }

    public Dict load(Long inventoryId, Long merchantId, Long organizationId) {
        Tuple fetchFirst = bqf.selectFrom(qStockInventory).select(qStockInventory, qInOrder.code, qOutOrder.code)
                .leftJoin(qInOrder).on(qInOrder.id.eq(qStockInventory.inOrderId))
                .leftJoin(qOutOrder).on(qOutOrder.id.eq(qStockInventory.outOrderId))
                .where(qStockInventory.merchantId.eq(merchantId).and(qStockInventory.id.eq(inventoryId)).and(qStockInventory.organizationId.eq(organizationId))).fetchFirst();

        StockInventoryDto dto = BeanUtil.toBean(fetchFirst.get(qStockInventory), StockInventoryDto.class);
        dto.setOutOrderCode(fetchFirst.get(qOutOrder.code));
        dto.setInOrderCode(fetchFirst.get(qInOrder.code));
        ArrayList<Dict> collect = jqf.selectFrom(qStockInventoryItem)
                .select(qStockInventoryItem, qWarehouses.name, qProducts.code, qProducts.name,
                        qProducts.imgPath, qProducts.specification, qProductsCategory.name)
                .leftJoin(qProducts).on(qProducts.id.eq(qStockInventoryItem.productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStockInventoryItem.warehouseId).and(qWarehouses.merchantId.eq(merchantId)).and(qWarehouses.organizationId.eq(organizationId)))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId).and(qProductsCategory.merchantId.eq(merchantId)).and(qProductsCategory.organizationId.eq(organizationId)))
                .where(qStockInventoryItem.stockInventoryId.eq(inventoryId).and(qStockInventoryItem.merchantId.eq(merchantId)).and(qStockInventoryItem.organizationId.eq(organizationId)))
                .orderBy(qStockInventoryItem.id.asc())
                .fetch().stream().collect(ArrayList::new, (list, tuple) -> {
                    StockInventoryItem od = tuple.get(qStockInventoryItem);
                    Dict dict = Dict.create()
                            .set("id", od.getId())
                            .set("productsId", od.getProductsId())
                            .set("inventoryQuantity", od.getInventoryQuantity())
                            .set("sysQuantity", od.getSysQuantity())
                            .set("unitId", od.getUnitId())
                            .set("unitName", od.getUnitName())
                            .set("differ", NumberUtil.sub(od.getInventoryQuantity(), od.getSysQuantity()))
                            .set("warehouseId", od.getWarehouseId())
                            .set("warehousesName", tuple.get(qWarehouses.name))
                            .set("remark", od.getRemark())
                            .set("productsCode", tuple.get(qProducts.code))
                            .set("productsName", tuple.get(qProducts.name))
                            .set("unitId", od.getUnitId())
                            .set("unitName", od.getUnitName())
                            .set("orderUnitId", od.getUnitId())
                            .set("orderUnitName", od.getUnitName())
                            .set("specification", tuple.get(qProducts.specification))
                            .set("categoryName", tuple.get(qProductsCategory.name))
                            .set("imgPath", tuple.get(qProducts.imgPath));
                    list.add(dict);
                }, List::addAll);
        return Dict.create().set("inventory", dto).set("itemList", collect);
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
                    return qStockInventory.createDate.desc();
                } else {
                    return qStockInventory.createDate.asc();
                }
            } else if ("billDate".equals(this.sortCol)) {
                if (Constants.SORT_DESC.equals(this.sort)) {
                    return qStockInventory.billDate.desc();
                } else {
                    return qStockInventory.billDate.asc();
                }
            }
            return qStockInventory.billDate.desc();
        }

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qStockInventory.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qStockInventory.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qStockInventory.code.contains(filter));
            }
        }

        public void setStart(LocalDate start) {
            if (start != null) {
                builder.and(qStockInventory.stockDate.goe(start));
            }
        }

        public void setEnd(LocalDate end) {
            if (end != null) {
                builder.and(qStockInventory.stockDate.loe(end));
            }
        }
    }
}
