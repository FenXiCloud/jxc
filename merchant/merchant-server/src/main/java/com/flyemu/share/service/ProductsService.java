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
import com.flyemu.share.dto.ProductsDto;
import com.flyemu.share.dto.SelectProductsDto;
import com.flyemu.share.dto.SelectPurchaseProductsDto;
import com.flyemu.share.dto.UnitPrice;
import com.flyemu.share.entity.*;
import com.flyemu.share.form.ProductsForm;
import com.flyemu.share.repository.CustomersLevelPriceRepository;
import com.flyemu.share.repository.ProductsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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
    private final ProductsRepository productsRepository;
    private final CustomersLevelPriceRepository customersLevelPriceRepository;
    private final QCustomersLevelPrice qCustomersLevelPrice = QCustomersLevelPrice.customersLevelPrice;
    private final QCustomers qCustomers = QCustomers.customers;
    private final CodeSeedService codeSeedService;


    public PageResults<ProductsDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qProducts)
                .select(qProducts, qUnits.name, qProductsCategory.name)
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builders())
                .orderBy(qProducts.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<ProductsDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            ProductsDto dto = BeanUtil.toBean(tuple.get(qProducts), ProductsDto.class);
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setUnitName(tuple.get(qUnits.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    @Transactional
    public void save(ProductsForm productsForm, Long merchantId, Long organizationId) {
        Products products = productsForm.getProducts();
        if (products.getEnableMultiUnit()) {
            Assert.isTrue(CollUtil.isNotEmpty(products.getMultiUnit()), "开启多单位,必须选择一个副单位");
            Set<Long> checkUnit = new HashSet<>();
            checkUnit.add(products.getUnitId());
            for (UnitPrice up : products.getMultiUnit()) {
                if (up.getUnitId() != null) {
                    Assert.isFalse(checkUnit.contains(up.getUnitId()), up.getUnitName() + "单位,不能一致");
                }
            }
        }
        if (products.getId() != null) {
            Products original = productsRepository.getById(products.getId());
            if (!original.getUnitId().equals(products.getUnitId())) {
                //商品下过单都不行
                QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
                Assert.isFalse(bqf.selectFrom(qOrderDetail).where(qOrderDetail.productsId.eq(products.getId()).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId))).fetchCount() > 0, "订单已使用计量单位,不能变更~");
                 }
            if (original.getEnableMultiUnit()) {
                List<Long> unitIds;
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
                    Assert.isFalse(bqf.selectFrom(qOrderDetail).where(qOrderDetail.unitId.notIn(unitIds).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId))).fetchCount() > 0, "订单已使用多单位,不能变更~");
                }
            }
            if (!original.getName().equals(products.getName())) {
                original.setPinyin(PinYinUtil.getFirstLettersLo(products.getName()) + "," + PinYinUtil.getPinyinString(products.getName()));
            }
            BeanUtil.copyProperties(products, original, CopyOptions.create().ignoreNullValue());
            products = productsRepository.save(original);

        } else {

            products.setOrganizationId(organizationId);
            products.setMerchantId(merchantId);
            if (StrUtil.isNotBlank(products.getCode())) {
                Assert.isFalse(bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId).and(qProducts.organizationId.eq(organizationId))
                        .and(qProducts.code.eq(products.getCode()))).fetchCount() > 0, products.getCode() + "商品编码已存在~");
            } else {
                ProductsCategory category = jqf.selectFrom(qProductsCategory).where(qProductsCategory.id.eq(products.getCategoryId()).and(qProductsCategory.merchantId.eq(merchantId)).and(qProductsCategory.organizationId.eq(organizationId))).fetchFirst();
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
                levelPrice.setPrice(cp.getBigDecimal("price"));
                levelPrice.setMerchantId(merchantId);
                levelPrice.setOrganizationId(organizationId);
                levelPrice.setCustomersLevelId(cp.getLong("customLeveId"));
                if (products.getEnableMultiUnit()) {
                    List<UnitPrice> ups = new ArrayList<>();
                    for (UnitPrice multiUnit : products.getMultiUnit()) {
                        UnitPrice up = new UnitPrice();
                        up.setUnitName(multiUnit.getUnitName());
                        up.setUnitId(multiUnit.getUnitId());
                        up.setPrice(cp.getBigDecimal(multiUnit.getUnitId() + ""));
                        ups.add(up);
                    }
                    levelPrice.setUnitPrice(ups);
                }
                cps.add(levelPrice);
            }
            jqf.delete(qCustomersLevelPrice).where(qCustomersLevelPrice.productsId.eq(products.getId()).and(qCustomersLevelPrice.merchantId.eq(merchantId)).and(qCustomersLevelPrice.organizationId.eq(organizationId))).
                    execute();
            customersLevelPriceRepository.saveAll(cps);
        } else {
            List<CustomersLevelPrice> priceList = jqf.selectFrom(qCustomersLevelPrice).where(qCustomersLevelPrice.productsId.eq(products.getId()).and(qCustomersLevelPrice.merchantId.eq(merchantId)).and(qCustomersLevelPrice.organizationId.eq(organizationId))).fetch();
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
                                up.setPrice(BigDecimal.ZERO);
                                price.getUnitPrice().add(up);
                            }
                        } else {
                            List<UnitPrice> ups = new ArrayList<>();
                            for (UnitPrice multiUnit : products.getMultiUnit()) {
                                UnitPrice up = new UnitPrice();
                                up.setUnitName(multiUnit.getUnitName());
                                up.setUnitId(multiUnit.getUnitId());
                                up.setNum(gp.getNum());
                                up.setPrice(BigDecimal.ZERO);
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
    public void delete(Long productsId, Long merchantId,Long organizationId) {

        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        Assert.isFalse(bqf.selectFrom(qOrderDetail).where(qOrderDetail.productsId.eq(productsId).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId))).fetchCount() > 0, "订单已使用,不能删除~");


        jqf.delete(qCustomersLevelPrice)
                .where(qCustomersLevelPrice.productsId.eq(productsId).and(qCustomersLevelPrice.merchantId.eq(merchantId)).and(qCustomersLevelPrice.organizationId.eq(organizationId)))
                .execute();
        jqf.delete(qProducts)
                .where(qProducts.id.eq(productsId).and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId)))
                .execute();
    }

    public List<Products> select(Long merchantId,Long organizationId) {
        return bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId).and(qProducts.organizationId.eq(organizationId))
                .and(qProducts.enabled.isTrue())).fetch();
    }

    public List<SelectPurchaseProductsDto> loadToOrder( Long merchantId, Long organizationId) {
        List<SelectPurchaseProductsDto> dtoList = bqf.selectFrom(qProducts)
                .select(qProducts.name, qProducts.code,  qProducts.specification, qProducts.id, qProductsCategory.path, qProducts.imgPath, qProducts.enableMultiUnit, qProducts.multiUnit, qProducts.unitId, qUnits.name)
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(qProducts.merchantId.eq(merchantId).and(qProducts.enabled.isTrue()).and(qProducts.organizationId.eq(organizationId)))
                .orderBy(qProducts.sort.desc(), qProducts.id.desc())
                .fetch().stream().collect(ArrayList::new, (list, tuple) -> {
                    SelectPurchaseProductsDto dto = new SelectPurchaseProductsDto();
                    dto.setProductsId(tuple.get(qProducts.id));
                    dto.setImgPath(tuple.get(qProducts.imgPath));
                    dto.setProductsCode(tuple.get(qProducts.code));
                    dto.setProductsName(tuple.get(qProducts.name));
                    dto.setPath(tuple.get(qProductsCategory.path));
                    dto.setSpec(tuple.get(qProducts.specification));
                    dto.setUnitName(tuple.get(qUnits.name));
                    dto.setUnitId(tuple.get(qProducts.unitId));
                    dto.setPrice(BigDecimal.ZERO);
                    List<UnitPrice> units = tuple.get(qProducts.multiUnit);

                    if (CollUtil.isNotEmpty(units) && tuple.get(qProducts.enableMultiUnit)) {
                        units.add(0, new UnitPrice(dto.getUnitId(), dto.getUnitName(), true, 1d, dto.getPrice()));
                        dto.setUnitPrice(units);
                    }
                    dto.setTitle();
                    list.add(dto);
                }, List::addAll);

        return dtoList;
    }
    public Products load(Long productsId, Long merchantId) {
        return jqf.selectFrom(qProducts).where(qProducts.id.eq(productsId).and(qProducts.merchantId.eq(merchantId))).fetchFirst();
    }



    public Map<Long, CustomersLevelPrice> levelPrice(Long productsId, Long merchantId,Long organizationId) {
        return jqf.selectFrom(qCustomersLevelPrice).where(qCustomersLevelPrice.productsId.eq(productsId).and(qCustomersLevelPrice.merchantId.eq(merchantId)).and(qCustomersLevelPrice.organizationId.eq(organizationId))).fetch().stream().collect(Collectors.toMap(c -> c.getCustomersLevelId(), b -> b));
    }
    
    public long findCount(Long merchantId) {
        return bqf.selectFrom(qProducts).where(qProducts.merchantId.eq(merchantId)).fetchCount();
    }

    public List<SelectProductsDto> goodsPriceList(Long customersId, Long merchantId, Long organizationId, List<Long> goodsIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProducts.enabled.isTrue());
//        if (CollUtil.isNotEmpty(goodsIds)) {
//            builder.and(qProducts.id.in(goodsIds));
//        }

        //等级价
        Map<Long, CustomersLevelPrice> customersLevelPriceMap = new HashMap<>();
        jqf.selectFrom(qCustomersLevelPrice)
                .innerJoin(qCustomers).on(qCustomers.customersLevelId.eq(qCustomersLevelPrice.customersLevelId))
                .innerJoin(qProducts).on(qProducts.id.eq(qCustomersLevelPrice.productsId).and(qProducts.merchantId.eq(merchantId)))
                .where(qCustomersLevelPrice.merchantId.eq(merchantId).and(qCustomersLevelPrice.organizationId.eq(organizationId)).and(qCustomers.id.eq(customersId)).and(builder)).fetch().forEach(tuple -> {
                    customersLevelPriceMap.put(tuple.getProductsId(), tuple);
                });

        List<SelectProductsDto> goodsDtoList = new ArrayList<>();
        jqf.selectFrom(qProducts)
                .select(qProducts.id, qProducts.code, qProducts.name, qProducts.pinyin,  qProducts.enableMultiUnit, qProducts.imgPath, qUnits.name,
                        qProducts.multiUnit, qProductsCategory.path, qProductsCategory.name, qProducts.unitId, qProducts.specification)
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId).and(qUnits.merchantId.eq(merchantId)))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId).and(qProductsCategory.merchantId.eq(merchantId)))
                .where(qProducts.merchantId.eq(merchantId).and(qProducts.organizationId.eq(organizationId)).and(builder)).orderBy(qProducts.sort.desc()).fetch().forEach(tuple -> {
                    SelectProductsDto dto = new SelectProductsDto();
                    dto.setProductsId(tuple.get(qProducts.id));
                    dto.setImgPath(tuple.get(qProducts.imgPath));
                    dto.setProductsCode(tuple.get(qProducts.code));
                    dto.setProductsName(tuple.get(qProducts.name));
                    dto.setPinyin(tuple.get(qProducts.pinyin));
                    dto.setCategory(tuple.get(qProductsCategory.name));
                    dto.setPath(tuple.get(qProductsCategory.path));
                    dto.setSpecification(tuple.get(qProducts.specification));
                    dto.setUnitName(tuple.get(qUnits.name));
                    dto.setUnitId(tuple.get(qProducts.unitId));
                    dto.setOrderUnitId(tuple.get(qProducts.unitId));
                    dto.setOrderUnitName(tuple.get(qUnits.name));
                    dto.setOrderNum(1d);
                    dto.setEnableMultiUnit(tuple.get(qProducts.enableMultiUnit));
                    dto.setPrice(BigDecimal.ZERO);
                    CustomersLevelPrice lp = customersLevelPriceMap.get(dto.getProductsId());
                    dto.setPrice(lp.getPrice());
                    if(dto.getEnableMultiUnit()){
                        List<UnitPrice> units = tuple.get(qProducts.multiUnit);
                        if (CollUtil.isNotEmpty(units) && units != null&&lp != null && CollUtil.isNotEmpty(lp.getUnitPrice())) {
                            for (UnitPrice up : units) {
                                Optional<UnitPrice> first = lp.getUnitPrice().stream().filter(c -> c.getUnitId().equals(up.getUnitId())).findFirst();
                                if (first.isPresent()) {
                                    up.setPrice(Optional.ofNullable(first.get().getPrice()).orElse(BigDecimal.ZERO));
                                }
                            }
                            units.add(0, new UnitPrice(dto.getUnitId(), dto.getUnitName(), true, 1d, dto.getPrice()));
                            dto.setUnitPrice(units);
                        }
                    }

                    goodsDtoList.add(dto);
                });

        return goodsDtoList;
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

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qProducts.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qProducts.organizationId.eq(organizationId));
            }
        }

        public void setEnabled(Boolean enabled) {
            if (enabled != null) {
                builder.and(qProducts.enabled.eq(enabled));
            }
        }

        public BooleanBuilder builders() {
            if (StrUtil.isNotBlank(path)) {
                builder.and(qProductsCategory.path.like(path + "%"));
            }
            if (StrUtil.isNotBlank(filter) && StrUtil.isNotBlank(filter.trim())) {
                builder.and(qProducts.name.contains(filter)
                        .or(qProducts.code.contains(filter))
                        .or(qProducts.pinyin.contains(filter)));
            }
            if (enabled != null) {
                builder.and(qProducts.enabled.eq(enabled));
            }
            return builder;
        }

    }
}
