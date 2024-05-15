package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.StockItemDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.enums.StockType;
import com.flyemu.share.repository.StockItemRepository;
import com.flyemu.share.repository.StockRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @功能描述: 库存明细表
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockItemService extends AbsService {
    private final static QStock qStock = QStock.stock;
    private final static QStockItem qStockItem = QStockItem.stockItem;
    private final static QOrder qOrder = QOrder.order;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final StockRepository stockRepository;
    private final StockItemRepository stockItemRepository;
    private final static QCustomers qCustomers = QCustomers.customers;
    private final static QUnits qUnits = QUnits.units;
    private final static QVendors qVendors = QVendors.vendors;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;

    public PageResults<StockItemDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qStockItem).select(qStockItem, qUnits.name, qOrder.orderType, qCustomers.name, qVendors.name, qProducts.code, qProducts.name, qWarehouses.name, qProductsCategory.name, qOrder.billDate, qOrder.code)
                .leftJoin(qOrder).on(qOrder.id.eq(qStockItem.orderId))
                .leftJoin(qProducts).on(qProducts.id.eq(qStockItem.productsId))
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qVendors).on(qVendors.id.eq(qOrder.vendorsId))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStockItem.warehouseId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builder)
                .orderBy(qStockItem.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<StockItemDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockItemDto dto = BeanUtil.toBean(tuple.get(qStockItem), StockItemDto.class);
            if (dto.getStockType().equals(StockType.减)) {
                dto.setOutQuantity(dto.getQuantity());
                dto.setOutTotalAmount(dto.getTotalAmount());
            }
            if (dto.getStockType().equals(StockType.加)) {
                dto.setInQuantity(dto.getQuantity());
                dto.setInTotalAmount(dto.getTotalAmount());
            }
            dto.setCustomersName(tuple.get(qCustomers.name));
            dto.setUnitName(tuple.get(qUnits.name));
            dto.setVendorsName(tuple.get(qVendors.name));
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setWarehouseName(tuple.get(qWarehouses.name));
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setOrderCode(tuple.get(qOrder.code));
            dto.setBillDate(tuple.get(qOrder.billDate));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }


    //订单汇总-按客户
    public PageResults stockByProducts(StockQuery query, Page page) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("merchantId", query.getMerchantId());
        params.put("organizationId", query.getOrganizationId());
        if (null != query.start && null != query.end) {
            params.put("start", query.getStart());
            params.put("end", query.getEnd());
        }
        if (StrUtil.isNotEmpty(query.filter)) {
            params.put("filter", query.filter);
        }
        if (null != query.warehousesIds && !query.warehousesIds.isEmpty()) {
            Set<Long> wIds = Stream.of(query.warehousesIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            if (wIds.size() > 0) {
                params.put("warehousesIds", wIds);
            }
        }
        org.sagacity.sqltoy.model.Page sqlPage = new org.sagacity.sqltoy.model.Page(page.getSize(), page.getPage());
        org.sagacity.sqltoy.model.Page<Dict> summaryPage = lazyDao.findPageBySql(sqlPage, "stockByProducts", params, Dict.class);
        return new PageResults<>(summaryPage.getRows(), page, summaryPage.getRecordCount());
    }

    /**
     * 入库出库单更新库存信息
     *
     * @param orderId
     * @param merchantId
     * @param organizationId
     * @param type
     */
    public void change(Long orderId, Long merchantId, Long organizationId, String type) {
        Order order = bqf.selectFrom(qOrder).where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).fetchOne();
        List<OrderDetail> orderDetails = bqf.selectFrom(qOrderDetail).where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.organizationId.eq(organizationId)).and(qOrderDetail.merchantId.eq(merchantId))).fetch();
        List<StockItem> stockItems = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();
        Set<Long> pIds = new HashSet<>();
        Set<Long> wIds = new HashSet<>();
        if (CollUtil.isNotEmpty(orderDetails)) {
            orderDetails.forEach(od -> {
                StockItem stockItem = new StockItem();
                stockItem.setOrderId(orderId);
                stockItem.setMerchantId(merchantId);
                stockItem.setOrganizationId(organizationId);
                stockItem.setProductsId(od.getProductsId());
                stockItem.setQuantity(od.getSysQuantity());
                stockItem.setAvailableQuantity(od.getSysQuantity());
                stockItem.setBillDate(order.getBillDate());
                stockItem.setWarehouseId(od.getWarehouseId());
                stockItem.setTotalAmount(od.getDiscountedAmount());
                stockItem.setStockType(od.getStockType());
                stockItems.add(stockItem);
                Stock stock = stockMap.get(od.getProductsId() + "-" + od.getWarehouseId());
                if (stock == null) {
                    pIds.add(od.getProductsId());
                    wIds.add(od.getWarehouseId());
                    stock = new Stock();
                    stock.setOrganizationId(organizationId);
                    stock.setMerchantId(merchantId);
                    stock.setWarehouseId(od.getWarehouseId());
                    stock.setProductsId(od.getProductsId());
                    stock.setTotalQuantity(type.equals("加") ? od.getSysQuantity() : od.getSysQuantity().negate());
                } else {
                    stock.setTotalQuantity(stock.getTotalQuantity().add(type.equals("加") ? od.getSysQuantity() : od.getSysQuantity().negate()));
                }
                stockMap.put(od.getProductsId() + "-" + od.getWarehouseId(), stock);
            });

            List<Stock> stocks = bqf.selectFrom(qStock).where(qStock.productsId.in(pIds).and(qStock.warehouseId.in(wIds)).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).fetch();
            if (CollUtil.isNotEmpty(stocks)) {
                for (Stock stock : stocks) {
                    Stock item = stockMap.get(stock.getProductsId() + "-" + stock.getWarehouseId());
                    if (item != null) {
                        stock.setTotalQuantity(stock.getTotalQuantity().add(item.getTotalQuantity()));
                        stockMap.put(stock.getProductsId() + "-" + stock.getWarehouseId(), stock);
                    }
                }
            }
            stockRepository.saveAll(stockMap.values());
            stockItemRepository.saveAll(stockItems);

        }
    }


    /**
     * 入库单更新库存信息
     *
     * @param orderId
     * @param merchantId
     * @param organizationId
     */
    public void inChange(Long orderId, Long merchantId, Long organizationId) {
        Order order = bqf.selectFrom(qOrder).where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).fetchOne();
        List<OrderDetail> orderDetails = bqf.selectFrom(qOrderDetail).where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.organizationId.eq(organizationId)).and(qOrderDetail.merchantId.eq(merchantId))).fetch();
        List<StockItem> stockItems = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();
        Set<Long> pIds = orderDetails.stream().map(OrderDetail::getProductsId).collect(Collectors.toSet());
        Set<Long> wIds = orderDetails.stream().map(OrderDetail::getWarehouseId).collect(Collectors.toSet());

        bqf.selectFrom(qStock).where(qStock.productsId.in(pIds).and(qStock.warehouseId.in(wIds)).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).fetch().forEach(
                tuple -> {
                    stockMap.put(tuple.getProductsId() + "-" + tuple.getWarehouseId(), tuple);
                }
        );

        if (CollUtil.isNotEmpty(orderDetails)) {
            orderDetails.forEach(od -> {
                StockItem stockItem = new StockItem();
                stockItem.setOrderId(orderId);
                stockItem.setMerchantId(merchantId);
                stockItem.setOrganizationId(organizationId);
                stockItem.setProductsId(od.getProductsId());
                stockItem.setQuantity(od.getSysQuantity());
                stockItem.setAvailableQuantity(od.getSysQuantity());
                stockItem.setBillDate(order.getBillDate());
                stockItem.setWarehouseId(od.getWarehouseId());
                stockItem.setTotalAmount(od.getDiscountedAmount());
                stockItem.setStockType(od.getStockType());
                stockItem.setAvailableQuantity(od.getSysQuantity());
                stockItem.setUnitCost(NumberUtil.div(od.getDiscountedAmount(), od.getSysQuantity(), 2, RoundingMode.HALF_UP));
                Stock stock = stockMap.get(od.getProductsId() + "-" + od.getWarehouseId());
                if (stock == null) {
                    stock = new Stock();
                    stock.setOrganizationId(organizationId);
                    stock.setMerchantId(merchantId);
                    stock.setWarehouseId(od.getWarehouseId());
                    stock.setProductsId(od.getProductsId());
                    stock.setTotalQuantity(od.getSysQuantity());
                } else {
                    if(stock.getTotalQuantity().compareTo(BigDecimal.ZERO) < 0) {
                        //如果库存为负数，则比较入库数量和库存的大小，库存大则可出库数量对于两者之和，否则为零
                        if (stock.getTotalQuantity().negate().compareTo(stockItem.getQuantity()) < 0) {
                            stockItem.setAvailableQuantity(stock.getTotalQuantity().add(od.getSysQuantity()));
                        }else {
                            stockItem.setAvailableQuantity(BigDecimal.ZERO);
                        }
                    }
                    stock.setTotalQuantity(stock.getTotalQuantity().add(od.getSysQuantity()));
                }
                stock.setInUnitCost(stockItem.getUnitCost());//更新最近入库单位成本
                stockItems.add(stockItem);
                stockMap.put(od.getProductsId() + "-" + od.getWarehouseId(), stock);
            });

            stockRepository.saveAll(stockMap.values());
            stockItemRepository.saveAll(stockItems);

        }
    }


    /**
     * 出库单更新库存信息
     *
     * @param orderId
     * @param merchantId
     * @param organizationId
     */
    public BigDecimal outChange(Long orderId, Long merchantId, Long organizationId) {
        Order order = bqf.selectFrom(qOrder).where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).fetchOne();
        List<OrderDetail> orderDetails = bqf.selectFrom(qOrderDetail).where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.organizationId.eq(organizationId)).and(qOrderDetail.merchantId.eq(merchantId))).fetch();
        List<StockItem> stockItems = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();
        final BigDecimal[] orderCost = {BigDecimal.ZERO};
        Set<Long> pIds = orderDetails.stream().map(OrderDetail::getProductsId).collect(Collectors.toSet());
        Set<Long> wIds = orderDetails.stream().map(OrderDetail::getWarehouseId).collect(Collectors.toSet());

        List<StockItem> stockItemList = bqf.selectFrom(qStockItem).where(qStockItem.availableQuantity.gt(0).and(qStockItem.productsId.in(pIds).and(qStockItem.warehouseId.in(wIds)))
                .and(qStockItem.stockType.eq(StockType.加)).and(qStockItem.merchantId.eq(merchantId)).and(qStockItem.organizationId.eq(organizationId))).fetch();

        List<StockItem> upStockItemList = new ArrayList<>();
        bqf.selectFrom(qStock).where(qStock.productsId.in(pIds).and(qStock.warehouseId.in(wIds)).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).fetch().forEach(
                tuple -> {
                    stockMap.put(tuple.getProductsId() + "-" + tuple.getWarehouseId(), tuple);
                }
        );
        if (CollUtil.isNotEmpty(orderDetails)) {
            orderDetails.forEach(od -> {
                StockItem stockItem = new StockItem();
                Stock stock = stockMap.get(od.getProductsId() + "-" + od.getWarehouseId());
                if(stock == null){
                    stock = new Stock();
                    stock.setOrganizationId(organizationId);
                    stock.setMerchantId(merchantId);
                    stock.setWarehouseId(od.getWarehouseId());
                    stock.setProductsId(od.getProductsId());
                    stock.setTotalQuantity(od.getSysQuantity().negate());

                    stockItem.setUnitCost(BigDecimal.ZERO);
                    stockItem.setCost(BigDecimal.ZERO);
                }else {

                    if (stock.getTotalQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                        stockItem.setUnitCost(stock.getInUnitCost());
                        stockItem.setCost(stock.getInUnitCost().multiply(od.getSysQuantity()));
                    }else {
                        List<StockItem> itemList = stockItemList.stream().filter(val->val.getProductsId().equals(od.getProductsId())&&val.getWarehouseId().equals(od.getWarehouseId())).collect(Collectors.toList());
                        if(CollUtil.isNotEmpty(itemList)) {
                            BigDecimal totalQuantity = od.getSysQuantity();
                            BigDecimal itemCost = BigDecimal.ZERO;
                            for (int i = 0; i < itemList.size(); i++) {
                                StockItem val = itemList.get(i);
                                if(val.getAvailableQuantity().compareTo(totalQuantity) >= 0) {
                                    itemCost = itemCost.add(NumberUtil.mul(totalQuantity, val.getUnitCost()));
                                    stockItem.setUnitCost(itemCost.divide(od.getSysQuantity(), 2, RoundingMode.HALF_UP));
                                    stockItem.setCost(itemCost);
                                    val.setAvailableQuantity(val.getAvailableQuantity().subtract(totalQuantity));
                                    upStockItemList.add(val);
                                    break;
                                }else {
                                    totalQuantity = totalQuantity.subtract(val.getAvailableQuantity());
                                    upStockItemList.add(val);
                                    if(i == itemList.size() - 1) {
                                        //最后一个 总成本除了加上最后一个入库记录的成本 还需要加上入库单位成本乘未出库的数量
                                        itemCost = itemCost.add(NumberUtil.mul(val.getAvailableQuantity(), val.getUnitCost())).add(NumberUtil.mul(totalQuantity, stock.getInUnitCost()));
                                        stockItem.setUnitCost(itemCost.divide(od.getSysQuantity(), 2, RoundingMode.HALF_UP));
                                        stockItem.setCost(itemCost);
                                    }else {
                                        itemCost = itemCost.add(NumberUtil.mul(val.getAvailableQuantity(), val.getUnitCost()));
                                    }
                                    val.setAvailableQuantity(BigDecimal.ZERO);
                                }
                            }
                        }
                    }
                }

                stock.setTotalQuantity(stock.getTotalQuantity().subtract(od.getSysQuantity()));

                stockMap.put(od.getProductsId() + "-" + od.getWarehouseId(), stock);

                stockItem.setOrderId(orderId);
                stockItem.setOrderDetailId(od.getId());
                stockItem.setMerchantId(merchantId);
                stockItem.setOrganizationId(organizationId);
                stockItem.setProductsId(od.getProductsId());
                stockItem.setQuantity(od.getSysQuantity());
                stockItem.setAvailableQuantity(BigDecimal.ZERO);
                stockItem.setBillDate(order.getBillDate());
                stockItem.setWarehouseId(od.getWarehouseId());
                stockItem.setTotalAmount(od.getDiscountedAmount());
                stockItem.setStockType(od.getStockType());
                orderCost[0] = orderCost[0].add(stockItem.getCost());
                stockItems.add(stockItem);
            });
            if(CollUtil.isNotEmpty(upStockItemList)){
                stockItemRepository.saveAll(upStockItemList);
            }
            stockRepository.saveAll(stockMap.values());
            stockItemRepository.saveAll(stockItems);
        }
        return orderCost[0];
    }

    /**
     * 调拨单更新库存信息
     *
     * @param orderId
     * @param merchantId
     * @param organizationId
     */
    public void changeTransfer(Long orderId, Long merchantId, Long organizationId) {
        Order order = bqf.selectFrom(qOrder).where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId))).fetchOne();
        List<OrderDetail> orderDetails = bqf.selectFrom(qOrderDetail).where(qOrderDetail.orderId.eq(orderId).and(qOrderDetail.organizationId.eq(organizationId)).and(qOrderDetail.merchantId.eq(merchantId))).fetch();
        List<StockItem> stockItems = new ArrayList<>();
        Map<String, Stock> stockMap = new HashMap<>();
        Set<Long> pIds = new HashSet<>();
        Set<Long> wIds = new HashSet<>(Arrays.asList(order.getOutWarehouseId(), order.getInWarehouseId()));
        if (CollUtil.isNotEmpty(orderDetails)) {
            orderDetails.forEach(od -> {
                StockItem inStockItem = new StockItem();
                inStockItem.setOrderId(orderId);
                inStockItem.setMerchantId(merchantId);
                inStockItem.setOrganizationId(organizationId);
                inStockItem.setProductsId(od.getProductsId());
                inStockItem.setQuantity(od.getOrderQuantity());
                inStockItem.setAvailableQuantity(od.getSysQuantity());
                inStockItem.setBillDate(order.getBillDate());
                inStockItem.setWarehouseId(order.getInWarehouseId());
                inStockItem.setTotalAmount(od.getDiscountedAmount());
                inStockItem.setStockType(StockType.加);
                inStockItem.setInventoryType(StockItem.InventoryType.调拨);
                stockItems.add(inStockItem);
                StockItem outStockItem = new StockItem();
                BeanUtil.copyProperties(inStockItem, outStockItem);
                outStockItem.setStockType(StockType.减);
                outStockItem.setWarehouseId(order.getOutWarehouseId());
                stockItems.add(outStockItem);

                Stock inStock = stockMap.get(od.getProductsId() + "-" + order.getInWarehouseId());
                if (inStock == null) {
                    pIds.add(od.getProductsId());
                    inStock = new Stock();
                    inStock.setOrganizationId(organizationId);
                    inStock.setMerchantId(merchantId);
                    inStock.setWarehouseId(order.getInWarehouseId());
                    inStock.setProductsId(od.getProductsId());
                    inStock.setTotalQuantity(od.getSysQuantity());
                } else {
                    inStock.setTotalQuantity(inStock.getTotalQuantity().add(od.getSysQuantity()));
                }
                stockMap.put(od.getProductsId() + "-" + order.getInWarehouseId(), inStock);


                Stock outStock = stockMap.get(od.getProductsId() + "-" + order.getOutWarehouseId());
                if (outStock == null) {
                    pIds.add(od.getProductsId());
                    outStock = new Stock();
                    outStock.setOrganizationId(organizationId);
                    outStock.setMerchantId(merchantId);
                    outStock.setWarehouseId(order.getOutWarehouseId());
                    outStock.setProductsId(od.getProductsId());
                    outStock.setTotalQuantity(od.getOrderQuantity().negate());
                } else {
                    outStock.setTotalQuantity(inStock.getTotalQuantity().add(od.getOrderQuantity().negate()));
                }
                stockMap.put(od.getProductsId() + "-" + order.getOutWarehouseId(), outStock);
            });

            List<Stock> stocks = bqf.selectFrom(qStock).where(qStock.productsId.in(pIds).and(qStock.warehouseId.in(wIds)).and(qStock.merchantId.eq(merchantId)).and(qStock.organizationId.eq(organizationId))).fetch();
            if (CollUtil.isNotEmpty(stocks)) {
                for (Stock stock : stocks) {
                    Stock item = stockMap.get(stock.getProductsId() + "-" + stock.getWarehouseId());
                    if (item != null) {
                        stock.setTotalQuantity(stock.getTotalQuantity().add(item.getTotalQuantity()));
                        stockMap.put(stock.getProductsId() + "-" + stock.getWarehouseId(), stock);
                    }
                }
            }
            stockRepository.saveAll(stockMap.values());
            stockItemRepository.saveAll(stockItems);

        }
    }

    @Data
    public static class StockQuery {
        private Long merchantId;
        private Long organizationId;
        private LocalDate start;
        private LocalDate end;
        private String filter;
        private String warehousesIds;
    }

    @Data
    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qStockItem.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qStockItem.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qProducts.name.contains(filter).or(qProducts.code.contains(filter)).or(qProducts.pinyin.contains(filter)));
            }
        }

        public void setWarehousesIds(String warehousesIds) {
            if (StrUtil.isNotEmpty(warehousesIds)) {
                Set<Long> wIds = Stream.of(warehousesIds.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
                if (wIds.size() > 0) {
                    builder.and(qStockItem.warehouseId.in(wIds));
                }
            }
        }

        public void setStart(String start) {
            if (StrUtil.isNotBlank(start)) {
                LocalDate parse = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                builder.and(qOrder.billDate.goe(parse));
            }
        }

        public void setEnd(String end) {
            if (StrUtil.isNotBlank(end)) {
                LocalDate parse = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                builder.and(qOrder.billDate.loe(parse));
            }
        }
    }
}
