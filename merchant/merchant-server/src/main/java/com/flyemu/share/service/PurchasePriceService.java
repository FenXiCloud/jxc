package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.flyemu.share.dto.PurchaserPriceDto;
import com.flyemu.share.dto.UnitPrice;
import com.flyemu.share.entity.Products;
import com.flyemu.share.entity.PurchasePrice;
import com.flyemu.share.entity.QProducts;
import com.flyemu.share.entity.QPurchasePrice;
import com.flyemu.share.repository.PurchasePriceRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @功能描述: 供货商价格
 * @创建时间: 2023年11月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchasePriceService extends AbsService {

    private final QPurchasePrice qPurchasePrice = QPurchasePrice.purchasePrice;
    private final QProducts qProducts = QProducts.products;

    private final PurchasePriceRepository ppr;

    @Transactional
    public void save(PurchaserPriceDto price) {
        Tuple tuple = bqf.selectFrom(qProducts)
                .select(qPurchasePrice, qProducts)
                .leftJoin(qPurchasePrice).on(qPurchasePrice.vendorsId.eq(price.getVendorsId()).and(qPurchasePrice.productsId.eq(qProducts.id)).and(qPurchasePrice.merchantId.eq(price.getMerchantId())))
                .where(qProducts.id.eq(price.getProductsId()).and(qProducts.merchantId.eq(price.getMerchantId()))).fetchFirst();
        if (tuple.get(qPurchasePrice) != null) {
            PurchasePrice purchasePrice = tuple.get(qPurchasePrice);
            if (purchasePrice.getUnitId().equals(price.getInputUnitId())) {
                jqf.update(qPurchasePrice)
                        .set(qPurchasePrice.price, price.getInputPrice())
                        .where(qPurchasePrice.id.eq(purchasePrice.getId())).execute();
            } else {
                Boolean b = true;
                if (purchasePrice.getUnitPrice() != null && CollUtil.isNotEmpty(purchasePrice.getUnitPrice())) {
                    for (UnitPrice item : purchasePrice.getUnitPrice()) {
                        if (item.getUnitId().equals(price.getInputUnitId())) {
                            item.setPrice(price.getInputPrice());
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        UnitPrice unitPrice = new UnitPrice(price.getInputUnitId(), price.getInputUnitName(), false, 1d, price.getInputPrice());
                        purchasePrice.getUnitPrice().add(unitPrice);
                    }
                    jqf.update(qPurchasePrice)
                            .set(qPurchasePrice.unitPrice, purchasePrice.getUnitPrice())
                            .where(qPurchasePrice.id.eq(purchasePrice.getId())).execute();
                } else {
                    UnitPrice unitPrice = new UnitPrice(price.getInputUnitId(), price.getInputUnitName(), false, 1d, price.getInputPrice());
                    List<UnitPrice> unitPrices = new ArrayList<>();
                    unitPrices.add(unitPrice);
                    purchasePrice.setUnitPrice(unitPrices);
                    ppr.save(purchasePrice);
                }
            }
        } else {
            Products products = tuple.get(qProducts);

            if (products.getUnitId().equals(price.getInputUnitId())) {
                price.setUnitId(price.getInputUnitId());
                price.setPrice(price.getInputPrice());
            } else {
                price.setUnitId(products.getUnitId());
//                price.setPrice(price.getInputPrice());
                if (CollUtil.isNotEmpty(products.getMultiUnit()) && products.getEnableMultiUnit()) {
                    products.getMultiUnit().stream().filter(item -> item.getUnitId().equals(price.getInputUnitId())).forEach(item -> {
                        item.setPrice(price.getInputPrice());
                    });
                    price.setUnitPrice(products.getMultiUnit());
                } else {

                }
            }
            PurchasePrice purchasePrice = BeanUtil.toBean(price, PurchasePrice.class);
            ppr.save(purchasePrice);
        }
    }

}