package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.QOrganization;
import com.flyemu.share.entity.QWarehouses;
import com.flyemu.share.entity.Warehouses;
import com.flyemu.share.repository.WarehousesRepository;
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

    private final QWarehouses qWarehouses = QWarehouses.warehouses;

    private final WarehousesRepository warehousesRepository;

    public PageResults<Warehouses> query(Page page, Integer merchantId) {
        PagedList<Warehouses> fetchPage = bqf.selectFrom(qWarehouses).where(qWarehouses.merchantId.eq(merchantId))
                .orderBy(qWarehouses.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<>(fetchPage, page);
    }


    @Transactional
    public Warehouses save(Warehouses warehouses, Integer merchantId) {
        if (warehouses.getId() != null) {
            //更新
            Warehouses original = warehousesRepository.getById(warehouses.getId());
            BeanUtil.copyProperties(warehouses, original, CopyOptions.create().ignoreNullValue());
            return warehousesRepository.save(original);
        }
        warehouses.setMerchantId(merchantId);
        return warehousesRepository.save(warehouses);
    }


    @Transactional
    public void delete(Integer warehousesId) {
        QOrganization qOrganization = QOrganization.organization;
        long count = bqf.selectFrom(qOrganization).where(qOrganization.id.eq(warehousesId)).fetchCount();
        warehousesRepository.deleteById(warehousesId);
    }

    public List<Warehouses> select(Integer merchantId) {
        return bqf.selectFrom(qWarehouses).where(qWarehouses.merchantId.eq(merchantId)).fetch();
    }
}
