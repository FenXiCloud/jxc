package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.OrganizationDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.CheckoutRepository;
import com.flyemu.share.repository.OrganizationRepository;
import com.flyemu.share.repository.UnitsRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationService extends AbsService {

    private static final QOrganization qOrganization = QOrganization.organization;

    private static final QCheckout qCheckout = QCheckout.checkout;

    private final OrganizationRepository organizationRepository;

    private final CheckoutRepository checkoutRepository;

    private final CodeSeedService codeSeedService;

    private final QMerchant qMerchant = QMerchant.merchant;

    private final UnitsRepository unitsRepository;

    private final AdminService adminService;

    public PageResults<OrganizationDto> query(Page page, Query query) {
        PagedList<Organization> fetchPage = bqf.selectFrom(qOrganization).where(query.builder).orderBy(qOrganization.id.desc()).fetchPage(page.getOffset(),page.getOffsetEnd());

        List<OrganizationDto> dtos = new ArrayList<>();
        fetchPage.forEach(tuple -> {
            Organization organization = tuple;
            OrganizationDto organizationDto = BeanUtil.toBean(organization, OrganizationDto.class);
            dtos.add(organizationDto);
        });

        return new PageResults<>(dtos, page, fetchPage.getTotalSize());
    }


    /**
     * 保存/更新
     *
     * @param organizationForm
     * @return
     */
    @Transactional
    public Organization save(OrganizationDto organizationForm) {
        if (organizationForm.getId() != null) {
            //更新
            Organization original = organizationRepository.getById(organizationForm.getId());
            BeanUtil.copyProperties(organizationForm, original, CopyOptions.create().ignoreNullValue());
            return organizationRepository.save(original);
        }

        Organization organization = BeanUtil.toBean(organizationForm, Organization.class);
        organization.setEnabled(true);
        organization.setCreateDate(LocalDateTime.now());
        if (StrUtil.isEmpty(organization.getCode())) {
            //如果没有设置，则使用系统生成
            String merchantCode = bqf.selectFrom(qMerchant)
                    .select(qMerchant.code)
                    .where(qMerchant.id.eq(organization.getMerchantId()))
                    .fetchOne();
            Integer nextSeed = codeSeedService.next(organization.getMerchantId(), merchantCode);
            organization.setCode("S" + merchantCode + nextSeed);
        } else {
            //手动设置了编码，需要检查重复
            long count = bqf.selectFrom(qOrganization)
                    .where(qOrganization.merchantId.eq(organization.getMerchantId()).and(qOrganization.code.eq(organization.getCode())))
                    .fetchCount();
            Assert.isTrue(count == 0, organization.getCode() + "编码已存在~");
        }
        organizationRepository.save(organization);

        return organization;
    }

    /**
     * 删除
     *
     * @param merchantId
     * @param organizationId
     */
    @Transactional
    public void delete(Integer merchantId, Integer organizationId) {
        QOrder qOrder = QOrder.order;
        long count = bqf.selectFrom(qOrder).where(qOrder.organizationId.eq(organizationId)).fetchCount();
        Assert.isTrue(count == 0, "已被使用不能删除~");
        jqf.delete(qOrganization)
                .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.id.eq(organizationId)))
                .execute();
    }

    /**
     * 根据ID获取数据
     *
     * @param orgId
     * @return
     */
    public Organization loadById(Integer merchantId, Integer orgId) {
        return bqf.selectFrom(qOrganization)
                .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.id.eq(orgId)))
                .fetchFirst();
    }

    public List<Organization> listAll(Integer merchantId) {
        return bqf.selectFrom(qOrganization)
                .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.enabled.isTrue()))
                .orderBy(qOrganization.code.asc())
                .fetch();
    }

    public List<Organization> select(Integer merchantId) {
        return bqf.selectFrom(qOrganization).where(qOrganization.merchantId.eq(merchantId).and(qOrganization.enabled.isTrue())).fetch();
    }

    @Transactional
    public void update(Organization organization) {
        if (organization.getId() != null) {
            //更新
            Organization original = organizationRepository.getById(organization.getId());
            BeanUtil.copyProperties(organization, original, CopyOptions.create().ignoreNullValue());
            organizationRepository.save(original);
        }
    }

    /**
     * 查询条件
     */
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
