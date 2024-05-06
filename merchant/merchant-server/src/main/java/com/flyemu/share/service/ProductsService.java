package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.common.PinYinUtil;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.UnitPrice;
import com.flyemu.share.entity.*;
import com.flyemu.share.form.ProductsForm;
import com.flyemu.share.repository.CustomersLevelPriceRepository;
import com.flyemu.share.repository.CustomersLevelRepository;
import com.flyemu.share.repository.ProductsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


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
    private final ProductsRepository productsRepository;
    private final CustomersLevelPriceRepository customersLevelPriceRepository;
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
    public void save(ProductsForm productsForm, Integer merchantId, Integer organizationId) {
        Products products = productsForm.getProducts();
        if (products.getEnableMultiUnit()) {
            Assert.isTrue(CollUtil.isNotEmpty(products.getMultiUnit()), "开启多单位,必须选择一个副单位");
            Set<Integer> checkUnit = new HashSet<>();
            checkUnit.add(products.getUnitId());
            for (UnitPrice up : products.getMultiUnit()) {
                if (up.getUnitId() != null) {
                    Assert.isFalse(checkUnit.contains(up.getUnitId()), up.getUnitName() + "单位,不能一致");
                }
            }
        }
        if (products.getId() != null) {
            //购物车  order orderA 购货单表 购货单退货表
            Products original = productsRepository.getById(products.getId());
            if (!original.getUnitId().equals(products.getUnitId())) {
                //商品下过单都不行
                QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
                Assert.isFalse(bqf.selectFrom(qOrderDetail).where(qOrderDetail.productsId.eq(products.getId()).and(qOrderDetail.merchantId.eq(merchantId))).fetchCount() > 0, "订单已使用计量单位,不能变更~");
                 }
            if (original.getEnableMultiUnit()) {
                List<Integer> unitIds;
                if (!products.getEnableMultiUnit()) { //关闭多单位需要去检测
                    unitIds = original.getMultiUnit().stream().map(UnitPrice::getUnitId).collect(Collectors.toList());
                } else {//取有变动的单位去校验
                    unitIds = new ArrayList<>();
                    if (!original.getMultiUnit().equals(products.getMultiUnit())) {
                        List<UnitPrice> prices = products.getMultiUnit();
                        original.getMultiUnit().forEach(item -> {
                            if (!prices.contains(item)) {
                                unitIds.add(item.getUnitId());
                            }
                        });
                    }
                }
                if (CollUtil.isNotEmpty(unitIds)) {
                    //校验商品单位是不是已经使用
                    QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
                    Assert.isFalse(bqf.selectFrom(qOrderDetail).where(qOrderDetail.unitId.notIn(unitIds).and(qOrderDetail.merchantId.eq(merchantId))).fetchCount() > 0, "订单已使用多单位,不能变更~");
                }
            }
            if (!original.getName().equals(products.getName())) {
                original.setPinyin(PinYinUtil.getFirstLettersLo(products.getName()) + "," + PinYinUtil.getPinyinString(products.getName()));
            }
            BeanUtil.copyProperties(products, original, CopyOptions.create().ignoreNullValue());
            products = productsRepository.save(original);

        } else {
            if (StrUtil.isNotBlank(products.getCode())) {
                Assert.isFalse(bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId).and(qProducts.organizationId.eq(organizationId))
                        .and(qProducts.code.eq(products.getCode()))).fetchCount() > 0, products.getCode() + "商品编码已存在~");
            } else {
                ProductsCategory category = jqf.selectFrom(qProductsCategory).where(qProductsCategory.id.eq(products.getCategoryId()).and(qProductsCategory.merchantId.eq(merchantId))).fetchFirst();
                products.setCode(category.getCode() + String.format("%04d", codeSeedService.next(merchantId, "productsCode")));
            }
            products.setMerchantId(merchantId);
            products.setEnabled(true);
            products.setPinyin(PinYinUtil.getFirstLettersLo(products.getName()) + "," + PinYinUtil.getPinyinString(products.getName()));
            productsRepository.save(products);
        }

        if (CollUtil.isNotEmpty(productsForm.getLevelPriceList())) {
            List<CustomersLevelPrice> cps = new ArrayList<>();
            for (JSONObject cp : productsForm.getLevelPriceList()) {
                CustomersLevelPrice levelPrice = new CustomersLevelPrice();
                levelPrice.setProductsId(products.getId());
                levelPrice.setUnitId(products.getUnitId());
                levelPrice.setPrice(cp.getDouble("price"));
                levelPrice.setMerchantId(merchantId);
                levelPrice.setCustomersLevelId(cp.getInteger("customLeveId"));
                if (products.getEnableMultiUnit()) {
                    List<UnitPrice> ups = new ArrayList<>();
                    for (UnitPrice multiUnit : products.getMultiUnit()) {
                        UnitPrice up = new UnitPrice();
                        up.setUnitName(multiUnit.getUnitName());
                        up.setUnitId(multiUnit.getUnitId());
                        up.setPrice(cp.getDoubleValue(multiUnit.getUnitId() + ""));
                        ups.add(up);
                    }
                    levelPrice.setUnitPrice(ups);
                }
                cps.add(levelPrice);
            }
            jqf.delete(qCustomersLevelPrice).where(qCustomersLevelPrice.productsId.eq(products.getId()).and(qCustomersLevelPrice.merchantId.eq(merchantId))).
                    execute();
            customersLevelPriceRepository.saveAll(cps);
        } else {
            List<CustomersLevelPrice> priceList = jqf.selectFrom(qCustomersLevelPrice).where(qCustomersLevelPrice.productsId.eq(products.getId()).and(qCustomersLevelPrice.merchantId.eq(merchantId))).fetch();
            for (CustomersLevelPrice price : priceList) {
                price.setUnitId(products.getUnitId());
                if (products.getEnableMultiUnit()) {
                    for (int i = 0; i < products.getMultiUnit().size(); i++) {
                        UnitPrice gp = products.getMultiUnit().get(i);
                        if (CollUtil.isNotEmpty(price.getUnitPrice())) {
                            UnitPrice unitPrice = null;
                            if (price.getUnitPrice() != null && price.getUnitPrice().size() > i) {
                                unitPrice = price.getUnitPrice().get(i);
                            }
                            if (unitPrice != null) {
                                unitPrice.setUnitId(gp.getUnitId());
                                unitPrice.setUnitName(gp.getUnitName());
                                unitPrice.setNum(gp.getNum());
                            } else {
                                UnitPrice up = new UnitPrice();
                                up.setUnitId(gp.getUnitId());
                                up.setUnitName(gp.getUnitName());
                                up.setNum(gp.getNum());
                                up.setPrice(0d);
                                price.getUnitPrice().add(up);
                            }
                        } else {
                            List<UnitPrice> ups = new ArrayList<>();
                            for (UnitPrice multiUnit : products.getMultiUnit()) {
                                UnitPrice up = new UnitPrice();
                                up.setUnitName(multiUnit.getUnitName());
                                up.setUnitId(multiUnit.getUnitId());
                                up.setNum(gp.getNum());
                                up.setPrice(0d);
                                ups.add(up);
                            }
                            price.setUnitPrice(ups);
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(priceList)) {
                customersLevelPriceRepository.saveAll(priceList);
            }
        }
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

        public void setOrganizationId(Integer organizationId) {
            if (organizationId != null) {
                builder.and(qProducts.organizationId.eq(organizationId));
            }
        }

        public void setEnable(Boolean enabled) {
            if (enabled != null) {
                builder.and(qProducts.enabled.eq(enabled));
            }
        }

    }
}
