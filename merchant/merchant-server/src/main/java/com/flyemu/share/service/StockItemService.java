package com.flyemu.share.service;

import cn.hutool.core.collection.CollUtil;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.StockItemRepository;
import com.flyemu.share.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final static QOrder qOrder = QOrder.order;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final StockRepository stockRepository;
    private final StockItemRepository stockItemRepository;


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
                stockItem.setQuantity(od.getOrderQuantity());
                stockItem.setAvailableQuantity(od.getSysQuantity());
                stockItem.setBillDate(order.getBillDate());
                stockItem.setWarehouseId(od.getWarehouseId());
                stockItem.setTotalAmount(od.getDiscountedAmount());
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
                    stock.setTotalQuantity(type.equals("加") ? od.getSysQuantity() : od.getOrderQuantity().negate());
                } else {
                    stock.setTotalQuantity(stock.getTotalQuantity().add(type.equals("加") ? od.getSysQuantity() : od.getOrderQuantity().negate()));
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

}
