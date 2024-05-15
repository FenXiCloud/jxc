package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.CustomersLevel;
import com.flyemu.share.entity.QCustomersLevel;
import com.flyemu.share.entity.QCustomersLevelPrice;
import com.flyemu.share.entity.QOrganization;
import com.flyemu.share.repository.CustomersLevelRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @功能描述: 客户级别管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomersLevelService extends AbsService {

    private final static QCustomersLevel qCustomersLevel = QCustomersLevel.customersLevel;
    private final static QCustomersLevelPrice qCustomersLevelPrice = QCustomersLevelPrice.customersLevelPrice;

    private final CustomersLevelRepository customersLevelRepository;


    public List<CustomersLevel> query(Query query) {
        List<CustomersLevel> customersLevels = bqf.selectFrom(qCustomersLevel)
                .where(query.builder)
                .orderBy(qCustomersLevel.id.desc())
                .fetch();
        return customersLevels;
    }


    @Transactional
    public CustomersLevel save(CustomersLevel customersLevel) {
        if (customersLevel.getId() != null) {
            //更新
            CustomersLevel original = customersLevelRepository.getById(customersLevel.getId());

            //检查重复
            long count = bqf.selectFrom(qCustomersLevel)
                    .where(qCustomersLevel.merchantId.eq(original.getMerchantId()).and(qCustomersLevel.name.eq(customersLevel.getName()))
                            .and(qCustomersLevel.id.ne(customersLevel.getId())).and(qCustomersLevel.organizationId.eq(original.getOrganizationId())))
                    .fetchCount();
            Assert.isTrue(count == 0, customersLevel.getName() + "名称已存在~");
            BeanUtil.copyProperties(customersLevel, original, CopyOptions.create().ignoreNullValue());
            return customersLevelRepository.save(original);
        }


        //检查重复
        long count = bqf.selectFrom(qCustomersLevel)
                .where(qCustomersLevel.merchantId.eq(customersLevel.getMerchantId()).and(qCustomersLevel.name.eq(customersLevel.getName()))
                        .and(qCustomersLevel.organizationId.eq(customersLevel.getOrganizationId())))
                .fetchCount();
        Assert.isTrue(count == 0, customersLevel.getName() + "名称已存在~");
        return customersLevelRepository.save(customersLevel);
    }

    /**
     * 删除
     *
     * @param customersLevelId
     */
    @Transactional
    public void delete(Long customersLevelId, Long merchantId,Long organizationId) {
        Assert.isFalse(bqf.selectFrom(qCustomersLevelPrice).where(qCustomersLevelPrice.customersLevelId.eq(customersLevelId).and(qCustomersLevelPrice.merchantId.eq(merchantId)).and(qCustomersLevelPrice.organizationId.eq(organizationId))).fetchCount()>0,"等级已使用，不能删除");
        jqf.delete(qCustomersLevel).where(qCustomersLevel.id.eq(customersLevelId).and(qCustomersLevel.merchantId.eq(merchantId)).and(qCustomersLevel.organizationId.eq(organizationId))).execute();
    }

    public List<CustomersLevel> select(Long merchantId,Long organizationId) {
        return bqf.selectFrom(qCustomersLevel).where(qCustomersLevel.merchantId.eq(merchantId).and(qCustomersLevel.organizationId.eq(organizationId))).fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qCustomersLevel.merchantId.eq(merchantId));
            }
        }
        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qCustomersLevel.organizationId.eq(organizationId));
            }
        }
        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qCustomersLevel.name.contains(filter));
            }
        }
    }


}
