package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.*;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.CheckoutRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckoutService extends AbsService {

    private final static QOrder qOrder = QOrder.order;
    private final static QCheckout qCheckout = QCheckout.checkout;
    private final static QAdmin qAdmin = QAdmin.admin;
    private final static QOrganization qOrganization = QOrganization.organization;


    private final CheckoutRepository checkoutRepository;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public PageResults<Dict> query(Page page, Query query) {
        PagedList<Tuple> fetchPage = bqf.selectFrom(qCheckout)
                .select(qAdmin.name,qCheckout.checkDate,qCheckout.createDate).where(query.builder)
                .leftJoin(qAdmin).on(qCheckout.checkId.eq(qAdmin.id))
                .orderBy(qCheckout.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<Dict>  dicts = fetchPage.stream().collect(ArrayList::new,(list,tuple)->{
            Dict dict = new Dict().set("checkName",tuple.get(qAdmin.name))
                    .set("checkDate",tuple.get(qCheckout.checkDate))
                    .set("createDate",tuple.get(qCheckout.createDate));
            list.add(dict);
        },List::addAll);
        return new PageResults<>(dicts, page, fetchPage.getTotalSize());
    }


    /**
     * 保存/更新
     *
     * @param checkout
     * @return
     */
    @Transactional
    public Checkout save(Checkout checkout) {
        List<String> strings =  new ArrayList<>();
        bqf.selectFrom(qOrder)
                .select(qOrder.orderType,qOrder.id.count())
                .where(qOrder.billDate.loe(checkout.getCheckDate()).and(qOrder.orderStatus.eq(OrderStatus.已保存))
                .and(qOrder.merchantId.eq(checkout.getMerchantId())).and(qOrder.organizationId.eq(checkout.getOrganizationId()))).groupBy(qOrder.orderType).fetch().forEach(tuple -> {
                    String str = tuple.get(qOrder.orderType).toString()+"-未审核条数："+tuple.get(qOrder.id.count());
                    strings.add(str);
               });
       if(CollUtil.isNotEmpty(strings)){
           throw  new ServiceException(strings.toString());
       }
        Assert.isFalse(bqf.selectFrom(qCheckout)
                .where(qCheckout.checkDate.goe(checkout.getCheckDate()).and(qCheckout.merchantId.eq(checkout.getMerchantId())).and(qCheckout.organizationId.eq(checkout.getOrganizationId()))).fetchCount()>0,"结账日期不能小于等于上次结账日期");
        Assert.isFalse(bqf.selectFrom(qOrganization)
                .where(qOrganization.checkoutDate.goe(checkout.getCheckDate()).and(qOrganization.merchantId.eq(checkout.getMerchantId())).and(qOrganization.id.eq(checkout.getOrganizationId()))).fetchCount()>0,"结账日期不能小于等于系统启用日期");
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
