package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.VendorsDto;
import com.flyemu.share.entity.QVendors;
import com.flyemu.share.entity.QVendorsCategory;
import com.flyemu.share.entity.Vendors;
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

    public List<Vendors> select(Long merchantId) {
        return bqf.selectFrom(qVendors).where(qVendors.merchantId.eq(merchantId)).fetch();
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
