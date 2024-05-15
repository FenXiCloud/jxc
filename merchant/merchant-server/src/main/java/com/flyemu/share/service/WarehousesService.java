package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.WarehousesRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WarehousesService extends AbsService {

    private static final QWarehouses qWarehouses = QWarehouses.warehouses;
    private static final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private static final QStockItem qStockItem = QStockItem.stockItem;
    private static final QStockInventoryItem qStockInventoryItem = QStockInventoryItem.stockInventoryItem;

    private final WarehousesRepository warehousesRepository;

    public PageResults<Warehouses> query(Page page, Query query) {
        PagedList<Warehouses> fetchPage = bqf.selectFrom(qWarehouses).where(query.builder)
                .orderBy(qWarehouses.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<>(fetchPage, page);
    }


    @Transactional
    public Warehouses save(Warehouses warehouses) {
        // 如果是默认
        if (warehouses.getIsDefault()) {
            jqf.update(qWarehouses)
                    .set(qWarehouses.isDefault, false)
                    .where(qWarehouses.merchantId.eq(warehouses.getMerchantId()).and(qWarehouses.organizationId.eq(warehouses.getOrganizationId()))).execute();
        }
        if (warehouses.getId() != null) {
            //更新
            Warehouses original = warehousesRepository.getById(warehouses.getId());
            //检查重复
            long count = bqf.selectFrom(qWarehouses)
                    .where(qWarehouses.merchantId.eq(original.getMerchantId()).and(qWarehouses.code.eq(warehouses.getCode()))
                            .and(qWarehouses.id.ne(warehouses.getId())).and(qWarehouses.organizationId.eq(original.getOrganizationId())))
                    .fetchCount();
            Assert.isTrue(count == 0, warehouses.getCode() + "编码已存在~");
            BeanUtil.copyProperties(warehouses, original, CopyOptions.create().ignoreNullValue());
            return warehousesRepository.save(original);
        }
        //检查重复
        long count = bqf.selectFrom(qWarehouses)
                .where(qWarehouses.merchantId.eq(warehouses.getMerchantId()).and(qWarehouses.code.eq(warehouses.getCode()))
                        .and(qWarehouses.organizationId.eq(warehouses.getOrganizationId())))
                .fetchCount();
        Assert.isTrue(count == 0, warehouses.getCode() + "编码已存在~");
        return warehousesRepository.save(warehouses);
    }


    @Transactional
    public void delete(Long warehousesId, Long merchantId, Long organizationId) {
        Assert.isFalse(
                bqf.selectFrom(qOrderDetail)
                        .where(qOrderDetail.warehouseId.eq(warehousesId).and(qOrderDetail.merchantId.eq(merchantId)).and(qOrderDetail.organizationId.eq(organizationId))).fetchCount() > 0, "已使用，不能删除");
        Assert.isFalse(
                bqf.selectFrom(qStockInventoryItem)
                        .where(qStockInventoryItem.warehouseId.eq(warehousesId).and(qStockInventoryItem.merchantId.eq(merchantId)).and(qStockInventoryItem.organizationId.eq(organizationId))).fetchCount() > 0, "已使用，不能删除");
        Assert.isFalse(
                bqf.selectFrom(qStockItem)
                        .where(qStockItem.warehouseId.eq(warehousesId).and(qStockItem.merchantId.eq(merchantId)).and(qStockItem.organizationId.eq(organizationId))).fetchCount() > 0, "已使用，不能删除");
        jqf.delete(qWarehouses)
                .where(qWarehouses.merchantId.eq(merchantId).and(qWarehouses.organizationId.eq(organizationId)).and(qWarehouses.id.eq(warehousesId))).execute();
    }

    public List<Warehouses> select(Long merchantId, Long organizationId) {
        return bqf.selectFrom(qWarehouses).where(qWarehouses.merchantId.eq(merchantId).and(qWarehouses.organizationId.eq(organizationId)).and(qWarehouses.enabled.isTrue())).fetch();
    }

    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qWarehouses.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qWarehouses.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (StrUtil.isNotBlank(filter)) {
                builder.and(qWarehouses.code.contains(filter)
                        .or(qWarehouses.name.contains(filter)));
            }
        }
    }
}
