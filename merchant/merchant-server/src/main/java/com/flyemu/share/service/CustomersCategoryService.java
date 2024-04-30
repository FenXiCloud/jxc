package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.CustomersCategory;
import com.flyemu.share.entity.QCustomers;
import com.flyemu.share.entity.QCustomersCategory;
import com.flyemu.share.repository.CustomersCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @功能描述: 客户分类管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomersCategoryService extends AbsService {

    private final static QCustomersCategory qCustomersCategory = QCustomersCategory.customersCategory;

    private final CustomersCategoryRepository customersCategoryRepository;



    public List<CustomersCategory> query(Query query) {
        List<CustomersCategory> customersCategorys = bqf.selectFrom(qCustomersCategory)
                .where(query.builder)
                .orderBy(qCustomersCategory.id.desc())
                .fetch();
        return customersCategorys;
    }


    @Transactional
    public CustomersCategory save(CustomersCategory customersCategory) {
        if (customersCategory.getId() != null) {
            //更新
            CustomersCategory original = customersCategoryRepository.getById(customersCategory.getId());
            BeanUtil.copyProperties(customersCategory, original, CopyOptions.create().ignoreNullValue());
            return customersCategoryRepository.save(original);
        }

        return customersCategoryRepository.save(customersCategory);
    }

    /**
     * 删除
     *
     * @param customersCategoryId
     */
    @Transactional
    public void delete(Integer customersCategoryId, Integer merchantId) {
        QCustomers qCustomers = QCustomers.customers;
        Assert.isFalse(bqf.selectFrom(qCustomers).where(qCustomers.merchantId.eq(merchantId).and(qCustomers.customersCategoryId.eq(customersCategoryId))).fetchCount() > 0, "已被客户使用，不能删除");

        jqf.delete(qCustomersCategory)
                .where(qCustomersCategory.id.eq(customersCategoryId).and(qCustomersCategory.merchantId.eq(merchantId)))
                .execute();
    }

    public List<CustomersCategory> select(Integer merchantId) {
        return bqf.selectFrom(qCustomersCategory).where(qCustomersCategory.merchantId.eq(merchantId)).fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qCustomersCategory.merchantId.eq(merchantId));
            }
        }
    }
}
