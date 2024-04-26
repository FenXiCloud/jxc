package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.Organization;
import com.flyemu.share.entity.QMerchant;
import com.flyemu.share.entity.QOrganization;
import com.flyemu.share.repository.OrganizationRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationService extends AbsService {

    private static final QOrganization qOrganization = QOrganization.organization;

    private static final QMerchant qMerchant = QMerchant.merchant;

    private final OrganizationRepository organizationRepository;

    private final CodeSeedService codeSeedService;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public PageResults<Organization> query(Page page, Query query) {
        PagedList<Organization> fetchPage = bqf.selectFrom(qOrganization)
                .where(query.builder)
                .orderBy(qOrganization.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<>(fetchPage, page);
    }

    /**
     * 保存/更新
     *
     * @param organization
     * @return
     */
    @Transactional
    public Organization save(Organization organization) {
        if (organization.getId() != null) {
            //更新
            Organization original = organizationRepository.getById(organization.getId());
            BeanUtil.copyProperties(organization, original, CopyOptions.create().ignoreNullValue());
            return organizationRepository.save(original);
        }
        organization.setEnabled(true);
        organization.setCreateDate(LocalDateTime.now());
        if (StrUtil.isEmpty(organization.getCode())) {
            //如果没有设置，则使用系统生成
            String merchantCode = bqf.selectFrom(qMerchant)
                    .select(qMerchant.code)
                    .where(qMerchant.id.eq(organization.getMerchantId()))
                    .fetchOne();
            Integer nextSeed = codeSeedService.next(organization.getMerchantId(), merchantCode);
            organization.setCode( merchantCode + nextSeed);
        } else {
            //手动设置了编码，需要检查重复
            long count = bqf.selectFrom(qOrganization)
                    .where(qOrganization.merchantId.eq(organization.getMerchantId()))
                    .fetchCount();
            Assert.isTrue(count == 0, organization.getCode() + "编码已存在~");
        }
        return organizationRepository.save(organization);
    }

    /**
     * 删除
     *
     * @param organizationId
     */
    @Transactional
    public void delete(Integer organizationId) {
        organizationRepository.deleteById(organizationId);
    }

    public List<Organization> listAll(Integer merchantId) {
        return bqf.selectFrom(qOrganization)
                .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.enabled.isTrue()))
                .orderBy(qOrganization.code.asc())
                .fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setName(String name) {
            if (StrUtil.isNotEmpty(name)) {
                builder.and(qOrganization.name.contains(name));
            }
        }

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qOrganization.merchantId.eq(merchantId));
            }
        }
    }
}
