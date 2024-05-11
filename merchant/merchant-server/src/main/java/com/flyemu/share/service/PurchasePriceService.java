package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.flyemu.share.dto.PurchasePriceRecordsDto;
import com.flyemu.share.dto.UnitPrice;
import com.flyemu.share.entity.Products;
import com.flyemu.share.entity.PurchasePriceRecords;
import com.flyemu.share.entity.QProducts;
import com.flyemu.share.entity.QPurchasePriceRecords;
import com.flyemu.share.repository.PurchasePriceRecordsRepository;
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

    private final QPurchasePriceRecords purchasePriceRecords = QPurchasePriceRecords.purchasePriceRecords;
    private final QProducts qProducts = QProducts.products;
    private final PurchasePriceRecordsRepository ppr;

    @Transactional
    public void save(PurchasePriceRecordsDto price) {
        Tuple tuple = bqf.selectFrom(qProducts)
                .select(purchasePriceRecords, qProducts)
                .leftJoin(purchasePriceRecords).on(purchasePriceRecords.vendorsId.eq(price.getVendorsId()).and(purchasePriceRecords.productsId.eq(qProducts.id)).and(purchasePriceRecords.merchantId.eq(price.getMerchantId())))
                .where(qProducts.id.eq(price.getProductsId()).and(qProducts.merchantId.eq(price.getMerchantId()))).fetchFirst();
        if (tuple.get(purchasePriceRecords) != null) {
            PurchasePriceRecords purchasePriceRecords = tuple.get(this.purchasePriceRecords);
            if (purchasePriceRecords.getUnitId().equals(price.getInputUnitId())) {
                jqf.update(this.purchasePriceRecords)
                        .set(this.purchasePriceRecords.price, price.getInputPrice())
                        .where(this.purchasePriceRecords.id.eq(purchasePriceRecords.getId())).execute();
            } else {
                Boolean b = true;
                if (purchasePriceRecords.getUnitPrice() != null && CollUtil.isNotEmpty(purchasePriceRecords.getUnitPrice())) {
                    for (UnitPrice item : purchasePriceRecords.getUnitPrice()) {
                        if (item.getUnitId().equals(price.getInputUnitId())) {
                            item.setPrice(price.getInputPrice());
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        UnitPrice unitPrice = new UnitPrice(price.getInputUnitId(), price.getInputUnitName(), false, 1d, price.getInputPrice());
                        purchasePriceRecords.getUnitPrice().add(unitPrice);
                    }
                    jqf.update(this.purchasePriceRecords)
                            .set(this.purchasePriceRecords.unitPrice, purchasePriceRecords.getUnitPrice())
                            .where(this.purchasePriceRecords.id.eq(purchasePriceRecords.getId())).execute();
                } else {
                    UnitPrice unitPrice = new UnitPrice(price.getInputUnitId(), price.getInputUnitName(), false, 1d, price.getInputPrice());
                    List<UnitPrice> unitPrices = new ArrayList<>();
                    unitPrices.add(unitPrice);
                    purchasePriceRecords.setUnitPrice(unitPrices);
                    ppr.save(purchasePriceRecords);
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
            PurchasePriceRecords purchasePriceRecords = BeanUtil.toBean(price, PurchasePriceRecords.class);
            ppr.save(purchasePriceRecords);
        }
    }

}