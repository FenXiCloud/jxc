package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.StockDto;
import com.flyemu.share.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @功能描述: 库存
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService extends AbsService {
    private final static QStock qStock = QStock.stock;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QUnits qUnits = QUnits.units;
    private final static QProducts qProducts = QProducts.products;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;

    public PageResults<StockDto> query(Page page, Query query) {

        PagedList<Tuple> pagedList = bqf.selectFrom(qStock)
                .select(qStock.productsId,qStock.totalQuantity.sum(), qUnits.name, qProducts.name, qProducts.code,qProducts.imgPath, qProductsCategory.name)
                .leftJoin(qProducts).on(qProducts.id.eq(qStock.productsId))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builders())
                .groupBy(qStock.productsId)
                .orderBy(qProducts.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        Set<Long> pIds = new HashSet<>();
        ArrayList<StockDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockDto dto = new StockDto();
            pIds.add(tuple.get(qStock.productsId));
            dto.setTreeId(tuple.get(qStock.productsId).toString());
            dto.setTotalQuantity(tuple.get(qStock.totalQuantity.sum()));
            dto.setWarehouseName("全部仓库");
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setImgPath(tuple.get(qProducts.imgPath));
            dto.setUnitsName(tuple.get(qUnits.name));
            list.add(dto);
        }, List::addAll);
        if(CollUtil.isEmpty(pIds)){
            return new PageResults<>(collect, page, pagedList.getTotalSize());
        }
        bqf.selectFrom(qStock)
                .select(qStock, qUnits.name, qProducts.name, qProducts.code,qProducts.imgPath, qProductsCategory.name, qWarehouses.name)
                .leftJoin(qProducts).on(qProducts.id.eq(qStock.productsId))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStock.warehouseId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builders().and(qStock.productsId.in(pIds)))
                .orderBy(qProducts.id.desc()).fetch().forEach(tuple -> {
                    StockDto dto = BeanUtil.toBean(tuple.get(qStock), StockDto.class);
                    dto.setTreeId("tr"+dto.getProductsId());
                    dto.setPTreeId(dto.getProductsId().toString());
                    dto.setWarehouseName(tuple.get(qWarehouses.name));
                    dto.setCategoryName(tuple.get(qProductsCategory.name));
                    dto.setProductsCode(tuple.get(qProducts.code));
                    dto.setProductsName(tuple.get(qProducts.name));
                    dto.setImgPath(tuple.get(qProducts.imgPath));
                    dto.setUnitsName(tuple.get(qUnits.name));
                    collect.add(dto);
                });
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }

    public PageResults<StockDto> adjustment(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qStock)
                .select(qStock.id,qStock.totalQuantity,qStock.weightedCost,qStock.productsId,qStock.warehouseId, qUnits.name,qWarehouses.name, qProducts.name, qProducts.code,qProducts.imgPath, qProductsCategory.name)
                .leftJoin(qProducts).on(qProducts.id.eq(qStock.productsId))
                .leftJoin(qUnits).on(qUnits.id.eq(qProducts.unitId))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qStock.warehouseId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builders().and(qStock.totalQuantity.gt(0)))
                .orderBy(qProducts.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<StockDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            StockDto dto = new StockDto();
            dto.setId(tuple.get(qStock.id));
            dto.setWarehouseId(tuple.get(qStock.warehouseId));
            dto.setProductsId(tuple.get(qStock.productsId));
            dto.setWeightedCost(tuple.get(qStock.weightedCost));
            dto.setTotalQuantity(tuple.get(qStock.totalQuantity));
            dto.setWarehouseName(tuple.get(qWarehouses.name));
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setImgPath(tuple.get(qProducts.imgPath));
            dto.setUnitsName(tuple.get(qUnits.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
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
                builder.and(qStock.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qStock.organizationId.eq(organizationId));
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
