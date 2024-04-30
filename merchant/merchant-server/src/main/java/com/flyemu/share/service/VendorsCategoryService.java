package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
            BeanUtil.copyProperties(vendorsCategory, original, CopyOptions.create().ignoreNullValue());
            return vendorsCategoryRepository.save(original);
        }

        return vendorsCategoryRepository.save(vendorsCategory);
    }


    @Transactional
    public void delete(Integer vendorsCategoryId, Integer merchantId) {
        jqf.delete(qVendorsCategory)
                .where(qVendorsCategory.id.eq(vendorsCategoryId).and(qVendorsCategory.merchantId.eq(merchantId)))
                .execute();
    }

    public List<VendorsCategory> select(Integer merchantId) {
        return bqf.selectFrom(qVendorsCategory).where(qVendorsCategory.merchantId.eq(merchantId)).fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qVendorsCategory.merchantId.eq(merchantId));
            }
        }
    }
}
