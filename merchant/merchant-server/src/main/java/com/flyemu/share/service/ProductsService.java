package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.ProductsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @功能描述: 商品列表
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductsService extends AbsService {

    private final static QProducts qProducts = QProducts.products;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;
    private final QUnits qUnits = QUnits.units;
    private final QVendors qVendors = QVendors.vendors;
    private final ProductsRepository ProductsRepository;
    private final QCustomersLevelPrice qCustomersLevelPrice = QCustomersLevelPrice.customersLevelPrice;
    private final QCustomersLevel qCustomersLevel = QCustomersLevel.customersLevel;
    private final CodeSeedService codeSeedService;


    public PageResults<Products> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qProducts)
                .select(qProducts, qUnits.name, qProductsCategory.name)
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builder)
                .orderBy(qProducts.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<Products> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            Products dto = BeanUtil.toBean(tuple.get(qProducts), Products.class);
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    @Transactional
    public void save(Products Products, Integer merchantId) {

    }

    @Transactional
    public void delete(Long productsId, Integer merchantId) {

    }

    public List<Products> select(Integer merchantId) {
        return bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId)).fetch();
    }

    public Products load(Long productsId, Integer merchantId) {
        return jqf.selectFrom(qProducts).where(qProducts.id.eq(productsId).and(qProducts.merchantId.eq(merchantId))).fetchFirst();
    }

    public long findCount(Integer merchantId) {
        return bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId)).fetchCount();
    }


    @Data
    public static class QuerySetup {

        private String filter;
        private Integer categoryId;

        private Integer levelId;
        private String path;
    }

    @Data
    public static class Query {

        public final BooleanBuilder builder = new BooleanBuilder();

        private String path;

        private String filter;

        private Integer categoryId;
        private Boolean enabled;

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qProducts.merchantId.eq(merchantId));
            }
        }

        public void setEnable(Boolean enabled) {
            if (enabled != null) {
                builder.and(qProducts.enabled.eq(enabled));
            }
        }

    }
}
