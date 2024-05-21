package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @功能描述: 销售出库单
 * @创建时间: 2024年05月09日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesOrderService extends AbsService {
    private final static QOrder qOrder = QOrder.order;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final static QCustomers qCustomers = QCustomers.customers;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QStockItem qStockItem = QStockItem.stockItem;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CodeSeedService codeSeedService;
    private final StockItemService stockItemService;


    public PageResults<PurchaserOrderDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qOrder).select(qOrder, qCustomers.name)
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .where(query.builder.and(qOrder.orderType.eq(OrderType.销售出库单)))
                .orderBy(query.sortSpecifier(), qOrder.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<PurchaserOrderDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            PurchaserOrderDto dto = BeanUtil.toBean(tuple.get(qOrder), PurchaserOrderDto.class);
            dto.setCustomersName(tuple.get(qCustomers.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }


    public PageResults<PurchaserOrderDto> profitList(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qOrder).select(qOrder, qCustomers.name)
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .where(query.builder.and(qOrder.orderType.eq(OrderType.销售出库单)).and(qOrder.orderStatus.eq(OrderStatus.已审核)))
                .orderBy(query.sortSpecifier(), qOrder.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<PurchaserOrderDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            PurchaserOrderDto dto = BeanUtil.toBean(tuple.get(qOrder), PurchaserOrderDto.class);
            dto.setCustomersName(tuple.get(qCustomers.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    //销售排行表-- 按商品
    public PageResults rankProducts(Page page, RankQuery query) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("merchantId", query.getMerchantId());
        params.put("organizationId", query.getOrganizationId());
        params.put("orderType", OrderType.销售出库单.name());
        params.put("orderStatus", OrderStatus.已审核.name());
        if (null != query.start && null != query.end) {
            params.put("start", query.getStart());
            params.put("end", query.getEnd());
        }
        if (StrUtil.isNotEmpty(query.filter)) {
            params.put("filter", query.filter);
        }
        org.sagacity.sqltoy.model.Page sqlPage = new org.sagacity.sqltoy.model.Page(page.getSize(), page.getPage());
        org.sagacity.sqltoy.model.Page<Dict> summaryPage = lazyDao.findPageBySql(sqlPage, "salesRankProducts", params, Dict.class);
        summaryPage.getRows().forEach(item -> {
            item.set("unitCost", item.getBigDecimal("cost").divide(item.getBigDecimal("sysQuantity"), 2, BigDecimal.ROUND_HALF_UP));
            item.set("profit", item.getBigDecimal("totalAmount").subtract(item.getBigDecimal("cost")));
            BigDecimal b = item.getBigDecimal("profit").divide(item.getBigDecimal("discountedAmount"), 4, BigDecimal.ROUND_HALF_UP);
            item.set("profitRatio", NumberUtil.mul(b, 100).doubleValue() + "%");
        });
        return new PageResults<>(summaryPage.getRows(), page, summaryPage.getRecordCount());
    }

    public BigDecimal queryTotal(Query query) {
        return bqf.selectFrom(qOrder)
                .select(qOrder.discountedAmount.sum())
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .where(query.builder).fetchFirst();
    }

    public List<Dict> listBy(Long customersId, Long merchantId, Long organizationId) {
        List<Dict> list = new ArrayList<>();
        bqf.selectFrom(qOrder).select(qOrder.code, qOrder.id)
                .where(qOrder.merchantId.eq(merchantId).and(qOrder.customersId.eq(customersId)).and(qOrder.organizationId.eq(organizationId)))
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
                if (d.getId() != null) {
                    ids.add(d.getId());
                }
                d.setStockType(StockType.减);
                d.setOrderId(order.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            orderDetailRepository.saveAll(orderForm.getDetailList());
            orderRepository.save(original);
        } else {
            String code = "XSCKD" + merchantCode + codeSeedService.dayIncrease(order.getMerchantId(), "XSCKD");
            order.setOrderType(OrderType.销售出库单);
            order.setCode(code);
            order.setOrderStatus(OrderStatus.已保存);
            order.setUserId(adminId);
            order.setMerchantId(merchantId);
            order.setOrganizationId(organizationId);
            orderRepository.save(order);
            for (OrderDetail d : orderForm.getDetailList()) {
                d.setStockType(StockType.减);
                d.setOrderId(order.getId());
                d.setMerchantId(merchantId);
                d.setOrganizationId(organizationId);
            }
            orderDetailRepository.saveAll(orderForm.getDetailList());
        }
    }

    public Dict load(Long merchantId, Long orderId, Long organizationId) {
        Tuple fetchFirst = jqf.selectFrom(qOrder).select(qOrder, qCustomers.name).leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId)).where(qOrder.merchantId.eq(merchantId).and(qOrder.id.eq(orderId)).and(qOrder.organizationId.eq(organizationId))).fetchFirst();

        PurchaserOrderDto order = BeanUtil.toBean(fetchFirst.get(qOrder), PurchaserOrderDto.class);
        order.setCustomersName(fetchFirst.get(qCustomers.name));
        ArrayList<Dict> collect = jqf.selectFrom(qOrderDetail)
                .select(qOrderDetail, qProducts.code, qProducts.name, qWarehouses.name,
                        qProducts.imgPath, qProducts.specification)
                .leftJoin(qProducts).on(qProducts.id.eq(qOrderDetail.productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrderDetail.warehouseId).and(qWarehouses.merchantId.eq(merchantId)).and(qWarehouses.organizationId.eq(organizationId)))
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
                            .set("warehouseName", tuple.get(qWarehouses.name))
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

    public Dict loadProfit(Long merchantId, Long orderId, Long organizationId) {
        Tuple fetchFirst = jqf.selectFrom(qOrder).select(qOrder, qCustomers.name).leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId)).where(qOrder.merchantId.eq(merchantId).and(qOrder.id.eq(orderId)).and(qOrder.organizationId.eq(organizationId))).fetchFirst();

        PurchaserOrderDto order = BeanUtil.toBean(fetchFirst.get(qOrder), PurchaserOrderDto.class);
        order.setCustomersName(fetchFirst.get(qCustomers.name));
        ArrayList<Dict> collect = jqf.selectFrom(qOrderDetail)
                .select(qOrderDetail, qProducts.code, qProducts.name, qWarehouses.name, qStockItem.cost, qStockItem.unitCost,
                        qProducts.imgPath, qProducts.specification)
                .leftJoin(qProducts).on(qProducts.id.eq(qOrderDetail.productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrderDetail.warehouseId).and(qWarehouses.merchantId.eq(merchantId)).and(qWarehouses.organizationId.eq(organizationId)))
                .leftJoin(qStockItem).on(qStockItem.orderDetailId.eq(qOrderDetail.id).and(qStockItem.orderId.eq(orderId)).and(qStockItem.merchantId.eq(merchantId)).and(qStockItem.organizationId.eq(organizationId)))
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
                            .set("cost", tuple.get(qStockItem.cost))
                            .set("unitCost", tuple.get(qStockItem.unitCost))
                            .set("unitId", od.getUnitId())
                            .set("unitName", od.getUnitName())
                            .set("orderUnitId", od.getOrderUnitId())
                            .set("orderUnitName", od.getOrderUnitName())
                            .set("discount", od.getDiscount())
                            .set("warehouseId", od.getWarehouseId())
                            .set("warehouseName", tuple.get(qWarehouses.name))
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
    public void updateState(Order order, Long adminId, Long merchantId, Long organizationId,LocalDate checkDate) {
        Order first = jqf.selectFrom(qOrder).where(qOrder.id.eq(order.getId()).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).fetchFirst();
        Assert.isFalse(first == null, "非法操作...");
        Assert.isTrue(first.getBillDate().isAfter(checkDate),"小于等于结账时间:"+checkDate+"不能修改数据");
        BigDecimal cost = stockItemService.outChange(order.getId(), merchantId, organizationId, "先");
        first.setCost(cost);
        first.setOrderStatus(OrderStatus.已审核);
        first.setCheckId(adminId);
        first.setCheckOutTime(LocalDateTime.now());
        orderRepository.save(first);
    }


    @Data
    public static class RankQuery {
        private Long merchantId;
        private Long organizationId;
        private LocalDate start;
        private LocalDate end;
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
                builder.and(qOrder.code.contains(filter).or(qCustomers.name.contains(filter)));
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
