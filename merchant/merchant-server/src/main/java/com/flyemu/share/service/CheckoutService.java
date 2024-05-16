package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.CheckoutRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckoutService extends AbsService {

    private final static QCheckout qCheckout = QCheckout.checkout;
    private final static QOrganization qOrganization = QOrganization.organization;


    private final CheckoutRepository checkoutRepository;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public PageResults<Checkout> query(Page page, Query query) {
        PagedList<Checkout> fetchPage = bqf.selectFrom(qCheckout).where(query.builder)
                .orderBy(qCheckout.checkDate.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults(fetchPage, page);
    }


    /**
     * 保存/更新
     *
     * @param checkout
     * @return
     */
    @Transactional
    public Checkout save(Checkout checkout) {
        jqf.update(qOrganization)
                .set(qOrganization.checkoutDate,checkout.getCheckDate())
                .where(qOrganization.merchantId.eq(checkout.getMerchantId()).and(qOrganization.id.eq(checkout.getOrganizationId()))).execute();
        if (checkout.getId() != null) {
            //更新
            Checkout original = checkoutRepository.getById(checkout.getId());
            BeanUtil.copyProperties(checkout, original, CopyOptions.create().ignoreNullValue());
            return checkoutRepository.save(original);
        }
        return checkoutRepository.save(checkout);
    }


    /**
     * 反结账
     *
     * @param organizationId
     * @param merchantId
     */
    @Transactional
    public LocalDate antiCheckout(Long organizationId,Long merchantId) {
       List<Checkout> checkouts =  bqf.selectFrom(qCheckout).where(qCheckout.organizationId.eq(organizationId).and(qCheckout.merchantId.eq(merchantId)))
                .orderBy(qCheckout.checkDate.desc()).fetch().stream().limit(2).collect(Collectors.toList());
       if (CollUtil.isNotEmpty(checkouts) && checkouts.size() > 1) {
           checkoutRepository.deleteById(checkouts.get(0).getId());

           jqf.update(qOrganization)
                   .set(qOrganization.checkoutDate,checkouts.get(1).getCheckDate())
                   .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.id.eq(organizationId))).execute();
           return checkouts.get(1).getCheckDate();
       }else {
           if (CollUtil.isNotEmpty(checkouts)) {
               checkoutRepository.deleteById(checkouts.get(0).getId());
           }
           LocalDate checkDate = bqf.selectFrom(qOrganization).select(qOrganization.startDate).where(qOrganization.merchantId.eq(merchantId).and(qOrganization.id.eq(organizationId))).fetchFirst();
           jqf.update(qOrganization)
                   .set(qOrganization.checkoutDate,checkDate)
                   .where(qOrganization.merchantId.eq(merchantId).and(qOrganization.id.eq(organizationId))).execute();
           return checkDate;
       }
    }

    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qCheckout.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qCheckout.organizationId.eq(organizationId));
            }
        }

    }
}
