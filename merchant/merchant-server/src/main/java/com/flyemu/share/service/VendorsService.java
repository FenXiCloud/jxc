package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.SelectPurchaseProductsDto;
import com.flyemu.share.dto.UnitPrice;
import com.flyemu.share.dto.VendorsDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.VendorsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @功能描述: 供货商管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorsService extends AbsService {

    private final static QVendors qVendors = QVendors.vendors;
    private final static QProducts qProducts = QProducts.products;
    private final static QPurchasePriceRecords qPurchasePriceRecords = QPurchasePriceRecords.purchasePriceRecords;
    private final static QUnits qUnits = QUnits.units;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;

    private final static QVendorsCategory qVendorsCategory = QVendorsCategory.vendorsCategory;

    private final VendorsRepository vendorsRepository;


    public PageResults query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qVendors)
                .select(qVendors, qVendorsCategory.name)
                .leftJoin(qVendorsCategory).on(qVendors.vendorsCategoryId.eq(qVendorsCategory.id))
                .where(query.builder)
                .orderBy(qVendors.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<VendorsDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            VendorsDto dto = BeanUtil.toBean(tuple.get(qVendors), VendorsDto.class);
            dto.setCategoryName(tuple.get(qVendorsCategory.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    @Transactional
    public Vendors save(Vendors vendors) {
        try {
            if (vendors.getId() != null) {
                //更新
                Vendors original = vendorsRepository.getById(vendors.getId());
                BeanUtil.copyProperties(vendors, original, CopyOptions.create().ignoreNullValue());
                return vendorsRepository.save(original);
            }
            return vendorsRepository.save(vendors);
        } catch (Exception e) {
            log.error("vendors save", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    public void delete(Long vendorsId, Long merchantId) {
        jqf.delete(qVendors)
                .where(qVendors.id.eq(vendorsId).and(qVendors.merchantId.eq(merchantId)))
                .execute();
    }

    public List<Vendors> select(Long merchantId,Long organizationId) {
        return bqf.selectFrom(qVendors).where(qVendors.merchantId.eq(merchantId).and(qVendors.organizationId.eq(organizationId)).and(qVendors.enabled.isTrue())).fetch();
    }

    public List<SelectPurchaseProductsDto> selectProducts(Long vendorsId, Long merchantId, Long organizationId) {
        List<SelectPurchaseProductsDto> dtoList = bqf.selectFrom(qProducts)
                .select(qProducts.name, qProducts.code, qPurchasePriceRecords.price, qPurchasePriceRecords.unitPrice, qProducts.specification, qProducts.id, qProductsCategory.path, qProducts.imgPath, qProducts.enableMultiUnit, qProducts.multiUnit, qProducts.unitId, qUnits.name)
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .leftJoin(qPurchasePriceRecords).on(qPurchasePriceRecords.productsId.eq(qProducts.id).and(qPurchasePriceRecords.vendorsId.eq(vendorsId)).and(qPurchasePriceRecords.merchantId.eq(merchantId)).and(qPurchasePriceRecords.organizationId.eq(organizationId)))
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
                    dto.setPrice(tuple.get(qPurchasePriceRecords.price));
                    List<UnitPrice> units = tuple.get(qProducts.multiUnit);


                    if (CollUtil.isNotEmpty(units) && tuple.get(qProducts.enableMultiUnit)) {
                        units.add(0, new UnitPrice(dto.getUnitId(), dto.getUnitName(), true, 1d, dto.getPrice()));
                        List<UnitPrice> finalUnits = units;
                        if (tuple.get(qPurchasePriceRecords.unitPrice) != null && CollUtil.isNotEmpty(tuple.get(qPurchasePriceRecords.unitPrice))) {
                            tuple.get(qPurchasePriceRecords.unitPrice).forEach(item -> {
                                finalUnits.forEach(unitPrice -> {
                                    if (item.getUnitId().equals(unitPrice.getUnitId())) {
                                        unitPrice.setPrice(item.getPrice());
                                    }
                                });
                            });
                        }
                        dto.setUnitPrice(finalUnits);
                    }
                    dto.setTitle();
                    list.add(dto);
                }, List::addAll);

        return dtoList;
    }

    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qVendors.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qVendors.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (StrUtil.isNotBlank(filter)) {
                builder.and(qVendors.code.contains(filter)
                        .or(qVendors.phone.contains(filter))
                        .or(qVendors.name.contains(filter)));
            }
        }
    }
}
