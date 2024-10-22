package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.QVendors;
import com.flyemu.share.entity.QVendorsCategory;
import com.flyemu.share.entity.VendorsCategory;
import com.flyemu.share.repository.VendorsCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @功能描述: 货商分类管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorsCategoryService extends AbsService {

    private final static QVendorsCategory qVendorsCategory = QVendorsCategory.vendorsCategory;
    private final static QVendors qVendors = QVendors.vendors;

    private final VendorsCategoryRepository vendorsCategoryRepository;


    public List<VendorsCategory> query(Query query) {
        List<VendorsCategory> vendorsCategorys = bqf.selectFrom(qVendorsCategory)
                .where(query.builder)
                .orderBy(qVendorsCategory.id.desc())
                .fetch();
        return vendorsCategorys;
    }


    @Transactional
    public VendorsCategory save(VendorsCategory vendorsCategory) {
        if (vendorsCategory.getId() != null) {
            //更新
            VendorsCategory original = vendorsCategoryRepository.getById(vendorsCategory.getId());

            //检查重复
            long count = bqf.selectFrom(qVendorsCategory)
                    .where(qVendorsCategory.merchantId.eq(original.getMerchantId()).and(qVendorsCategory.code.eq(vendorsCategory.getCode()))
                            .and(qVendorsCategory.id.ne(vendorsCategory.getId())).and(qVendorsCategory.organizationId.eq(original.getOrganizationId())))
                    .fetchCount();
            Assert.isTrue(count == 0, vendorsCategory.getCode() + "编码已存在~");
            BeanUtil.copyProperties(vendorsCategory, original, CopyOptions.create().ignoreNullValue());
            return vendorsCategoryRepository.save(original);
        }


        //检查重复
        long count = bqf.selectFrom(qVendorsCategory)
                .where(qVendorsCategory.merchantId.eq(vendorsCategory.getMerchantId()).and(qVendorsCategory.code.eq(vendorsCategory.getCode()))
                        .and(qVendorsCategory.organizationId.eq(vendorsCategory.getOrganizationId())))
                .fetchCount();
        Assert.isTrue(count == 0, vendorsCategory.getCode() + "编码已存在~");
        return vendorsCategoryRepository.save(vendorsCategory);
    }


    @Transactional
    public void delete(Long vendorsCategoryId, Long merchantId, Long organizationId) {
        Assert.isFalse(bqf.selectFrom(qVendors).where(qVendors.vendorsCategoryId.eq(vendorsCategoryId).and(qVendors.merchantId.eq(merchantId)).and(qVendors.organizationId.eq(organizationId))).fetchCount()>0,"分类已使用，不能删除");
        jqf.delete(qVendorsCategory)
                .where(qVendorsCategory.id.eq(vendorsCategoryId).and(qVendorsCategory.merchantId.eq(merchantId)).and(qVendorsCategory.organizationId.eq(organizationId)))
                .execute();
    }

    public List<VendorsCategory> select(Long merchantId, Long organizationId) {
        return bqf.selectFrom(qVendorsCategory).where(qVendorsCategory.merchantId.eq(merchantId).and(qVendorsCategory.organizationId.eq(organizationId))).fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qVendorsCategory.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qVendorsCategory.organizationId.eq(organizationId));
            }
        }
    }
}
