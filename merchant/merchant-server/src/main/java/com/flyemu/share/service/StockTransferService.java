package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.dto.PurchaserOrderDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.enums.OrderType;
import com.flyemu.share.enums.StockType;
import com.flyemu.share.form.OrderForm;
import com.flyemu.share.repository.OrderDetailRepository;
import com.flyemu.share.repository.OrderRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @功能描述: 调拨单
 * 调拨单做入库单处理，明细的仓库对应的是调入仓库
 * @创建时间: 2024年05月10日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockTransferService extends AbsService {
    private final static QOrder qOrder = QOrder.order;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QWarehouses qOutWarehouses = new QWarehouses("id");
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CodeSeedService codeSeedService;
    private final StockItemService stockItemService;


    public PageResults<PurchaserOrderDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qOrder).select(qOrder, qWarehouses.name, qOutWarehouses.name)
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrder.inWarehouseId))
                .leftJoin(qOutWarehouses).on(qOutWarehouses.id.eq(qOrder.outWarehouseId))
                .where(query.builder.and(qOrder.orderType.eq(OrderType.调拨单)))
                .orderBy(query.sortSpecifier(), qOrder.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<PurchaserOrderDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            PurchaserOrderDto dto = BeanUtil.toBean(tuple.get(qOrder), PurchaserOrderDto.class);
            dto.setInWarehouseName(tuple.get(qWarehouses.name));
            dto.setOutWarehouseName(tuple.get(qOutWarehouses.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    public BigDecimal queryTotal(Query query) {
        return bqf.selectFrom(qOrder)
                .select(qOrder.discountedAmount.sum())
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrder.inWarehouseId))
                .leftJoin(qOutWarehouses).on(qOutWarehouses.id.eq(qOrder.outWarehouseId))
                .where(query.builder.and(qOrder.orderType.eq(OrderType.调拨单))).fetchFirst();
    }

    public List<Dict> listBy(Long vendorsId, Long merchantId, Long organizationId) {
        List<Dict> list = new ArrayList<>();
        bqf.selectFrom(qOrder).select(qOrder.code, qOrder.id)
                .where(qOrder.merchantId.eq(merchantId).and(qOrder.vendorsId.eq(vendorsId)).and(qOrder.organizationId.eq(organizationId)))
                .orderBy(qOrder.id.desc()).fetch().forEach(tuple -> {
                    Dict dict = new Dict()
                            .set("id", tuple.get(qOrder.id))
                            .set("name", tuple.get(qOrder.code));
                    list.add(dict);
                });
        return list;
    }

    @Transactional
    public void delete(Long orderId, Long merchantId, Long organizationId) {
        jqf.delete(qOrder).where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).execute();
        jqf.delete(qOrderDetail).where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId))).execute();
    }

    @Transactional
    public void save(OrderForm orderForm, Long adminId, Long merchantId, Long organizationId, String merchantCode) {
        Order order = orderForm.getOrder();
        order.setMerchantId(merchantId);
        if (order.getId() != null) {
            Order original = orderRepository.getById(order.getId());

            BeanUtil.copyProperties(order, original, CopyOptions.create().ignoreNullValue());

            Set<Long> ids = new HashSet<>();
            for (OrderDetail d : orderForm.getDetailList()) {
                d.setWarehouseId(order.getInWarehouseId());
                if (d.getId() != null) {
                    ids.add(d.getId());
                }
                d.setStockType(StockType.平);
                d.setOrderId(order.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            orderDetailRepository.saveAll(orderForm.getDetailList());
            orderRepository.save(original);
        } else {
            String code = "";
            code = "CKDBD" + merchantCode + codeSeedService.dayIncrease(order.getMerchantId(), "CKDBD");
            order.setOrderType(OrderType.调拨单);
            order.setCode(code);
            order.setOrderStatus(OrderStatus.已保存);
            order.setUserId(adminId);
            order.setMerchantId(merchantId);
            order.setOrganizationId(organizationId);
            orderRepository.save(order);
            for (OrderDetail d : orderForm.getDetailList()) {
                d.setStockType(StockType.平);
                d.setWarehouseId(order.getInWarehouseId());
                d.setOrderId(order.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            orderDetailRepository.saveAll(orderForm.getDetailList());
        }
    }

    public Dict load(Long merchantId, Long orderId, Long organizationId) {
        Tuple fetchFirst = jqf.selectFrom(qOrder).select(qOrder, qWarehouses.name, qOutWarehouses.name)
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrder.inWarehouseId))
                .leftJoin(qOutWarehouses).on(qOutWarehouses.id.eq(qOrder.outWarehouseId))
                .where(qOrder.merchantId.eq(merchantId).and(qOrder.id.eq(orderId))
                        .and(qOrder.organizationId.eq(organizationId))).fetchFirst();

        PurchaserOrderDto order = BeanUtil.toBean(fetchFirst.get(qOrder), PurchaserOrderDto.class);
        order.setInWarehouseName(fetchFirst.get(qWarehouses.name));
        order.setOutWarehouseName(fetchFirst.get(qOutWarehouses.name));
        ArrayList<Dict> collect = jqf.selectFrom(qOrderDetail)
                .select(qOrderDetail, qProducts.code, qProducts.name,
                        qProducts.imgPath, qProducts.specification)
                .leftJoin(qProducts).on(qProducts.id.eq(qOrderDetail.productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId)))
                .orderBy(qOrderDetail.id.asc())
                .fetch().stream().collect(ArrayList::new, (list, tuple) -> {
                    OrderDetail od = tuple.get(qOrderDetail);
                    Dict dict = Dict.create()
                            .set("id", od.getId())
                            .set("productsId", od.getProductsId())
                            .set("discountedAmount", od.getDiscountedAmount())
                            .set("orderPrice", od.getOrderPrice())
                            .set("orderQuantity", od.getOrderQuantity())
                            .set("sysQuantity", od.getSysQuantity())
                            .set("unitId", od.getUnitId())
                            .set("unitName", od.getUnitName())
                            .set("orderUnitId", od.getOrderUnitId())
                            .set("orderUnitName", od.getOrderUnitName())
                            .set("discount", od.getDiscount())
                            .set("warehouseId", od.getWarehouseId())
                            .set("remark", od.getRemark())
                            .set("discountAmount", od.getDiscountAmount())
                            .set("productsCode", tuple.get(qProducts.code))
                            .set("productsName", tuple.get(qProducts.name))
                            .set("spec", tuple.get(qProducts.specification))
                            .set("imgPath", tuple.get(qProducts.imgPath));
                    list.add(dict);
                }, List::addAll);
        return Dict.create().set("order", order).set("productsData", collect);
    }


    @Transactional
    public void updateState(Order order,  AccountDto accountDto) {
        Order first = jqf.selectFrom(qOrder).where(qOrder.id.eq(order.getId()).and(qOrder.merchantId.eq(accountDto.getMerchantId())).and(qOrder.organizationId.eq(accountDto.getOrganizationId()))).fetchFirst();
        Assert.isFalse(first == null, "非法操作...");
        Assert.isTrue(first.getBillDate().isAfter(accountDto.getCheckDate()),"小于等于结账时间:"+accountDto.getCheckDate()+"不能修改数据");
        BigDecimal cost = stockItemService.changeToTransfer(order.getId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getCostMethod());
        first.setCost(cost);
        first.setOrderStatus(OrderStatus.已审核);
        first.setCheckId(accountDto.getAdminId());
        first.setCheckOutTime(LocalDateTime.now());
        orderRepository.save(first);
    }


    
    @Data
    public static class DetailQuery {
        private String goodsFilter;

        private String filter;

        private LocalDate start;

        private LocalDate end;
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
                    return qOrder.createDate.desc();
                } else {
                    return qOrder.createDate.asc();
                }
            } else if ("billDate".equals(this.sortCol)) {
                if (Constants.SORT_DESC.equals(this.sort)) {
                    return qOrder.billDate.desc();
                } else {
                    return qOrder.billDate.asc();
                }
            }
            return qOrder.billDate.desc();
        }

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qOrder.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qOrder.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qOrder.code.contains(filter).or(qWarehouses.name.contains(filter)).or(qOutWarehouses.name.contains(filter)));
            }
        }

        public void setStart(LocalDate start) {
            if (start != null) {
                builder.and(qOrder.billDate.goe(start));
            }
        }

        public void setEnd(LocalDate end) {
            if (end != null) {
                builder.and(qOrder.billDate.loe(end));
            }
        }
    }
}
