package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.QOrganization;
import com.flyemu.share.entity.QWarehouses;
import com.flyemu.share.entity.Warehouses;
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

    private final WarehousesRepository warehousesRepository;

    public PageResults<Warehouses> query(Page page,Query query) {
        PagedList<Warehouses> fetchPage = bqf.selectFrom(qWarehouses).where(query.builder)
                .orderBy(qWarehouses.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<>(fetchPage, page);
    }


    @Transactional
    public Warehouses save(Warehouses warehouses) {
        // 如果是默认
        if (warehouses.getIsDefault()){
            jqf.update(qWarehouses)
                    .set(qWarehouses.isDefault,false)
                    .where(qWarehouses.merchantId.eq(warehouses.getMerchantId()).and(qWarehouses.organizationId.eq(warehouses.getOrganizationId()))).execute();
        }
        if (warehouses.getId() != null) {
            //更新
            Warehouses original = warehousesRepository.getById(warehouses.getId());
            BeanUtil.copyProperties(warehouses, original, CopyOptions.create().ignoreNullValue());
            return warehousesRepository.save(original);
        }
        return warehousesRepository.save(warehouses);
    }


    @Transactional
    public void delete(Long warehousesId,Long merchantId,  Long organizationId) {
        jqf.delete(qWarehouses)
                .where(qWarehouses.merchantId.eq(merchantId).and(qWarehouses.organizationId.eq(organizationId)).and(qWarehouses.id.eq(warehousesId))).execute();
    }

    public List<Warehouses> select(Long merchantId,Long organizationId) {
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
