package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.Checkout;
import com.flyemu.share.entity.Order;
import com.flyemu.share.entity.QCheckout;
import com.flyemu.share.entity.QOrder;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.CheckoutRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckoutService extends AbsService {

    private final static QCheckout qCheckout = QCheckout.checkout;

    private final QOrder qOrder = QOrder.order;

    private final CheckoutRepository checkoutRepository;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public PageResults<Checkout> query(Page page, Query query) {
        PagedList<Checkout> fetchPage = bqf.selectFrom(qCheckout).where(query.builder)
                .orderBy(qCheckout.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<Checkout>(fetchPage, page);
    }


    /**
     * //门店未结账最小日期
     *
     * @param organizationId
     * @return
     */
    public Checkout queryMinDate(Long organizationId) {
        return bqf.selectFrom(qCheckout).where(qCheckout.organizationId.eq(organizationId).and(qCheckout.status.eq(Checkout.Status.已结账)))
                .orderBy(qCheckout.checkDate.asc())
                .fetchFirst();
    }

    /**
     * 保存/更新
     *
     * @param checkout
     * @return
     */
    @Transactional
    public Checkout save(Checkout checkout) {
        if (checkout.getId() != null) {
            //更新
            Checkout original = checkoutRepository.getById(checkout.getId());
            BeanUtil.copyProperties(checkout, original, CopyOptions.create().ignoreNullValue());
            return checkoutRepository.save(original);
        }
        return checkoutRepository.save(checkout);
    }

    /**
     * 删除
     *
     * @param checkoutId
     */
    @Transactional
    public void delete(Long checkoutId, Long merchantId) {

        long poCount = bqf.selectFrom(qCheckout).where(qCheckout.id.eq(checkoutId)
                .and(qCheckout.merchantId.eq(merchantId))).fetchCount();

        Assert.isFalse(poCount == 0, "非法操作~");

        checkoutRepository.deleteById(checkoutId);
    }


    @Transactional
    public List<Order> check(Checkout checkout, Long organizationId, Checkout minCheck) {
        Map<String, Object> result = new HashMap<>();
        if (checkout != null && (checkout.getCheckDate().isEqual(minCheck.getCheckDate()))) {
            //月末日期
            LocalDate monthEnd = checkout.getCheckDate().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()).toLocalDate();
            monthEnd = monthEnd.plusDays(-1);
            List<Order> fetch = bqf.selectFrom(qOrder).where(qOrder.billDate.between(checkout.getCheckDate(), monthEnd).and(qOrder.organizationId.eq(organizationId)))
                    .orderBy(qOrder.orderType.desc())
                    .fetch();
            if (CollUtil.isNotEmpty(fetch)) {
                return fetch;
            } else {
                jqf.update(qCheckout).set(qCheckout.status, Checkout.Status.已结账).where(qCheckout.id.eq(checkout.getId()).and(qCheckout.organizationId.eq(organizationId))).execute();
                //验证上期是否存在
                LocalDate plusMonths = checkout.getCheckDate().plusMonths(1);
                Checkout fetchFirst = bqf.selectFrom(qCheckout).where(qCheckout.organizationId.eq(organizationId).and(qCheckout.checkDate.eq(plusMonths))).fetchFirst();
                if (fetchFirst == null) {
                    Checkout out = new Checkout();
                    out.setCheckDate(plusMonths);
                    out.setCheckYear(out.getCheckDate().getYear());
                    out.setCheckMonth(out.getCheckDate().getMonthValue());
                    out.setOrganizationId(organizationId);
                    out.setMerchantId(checkout.getMerchantId());
                    out.setStatus(Checkout.Status.已结账);
                    checkoutRepository.save(out);
                }
            }
        } else {
            throw new ServiceException("上期未结账~");
        }
        return null;
    }

    /**
     * 反结账
     *
     * @param checkout
     * @param minDate
     */
    @Transactional
    public Checkout antiCheckout(Checkout checkout, Checkout minDate) {
        if (!checkout.getCheckDate().plusMonths(1).equals(minDate.getCheckDate())) {
            throw new ServiceException("上期已结账,当期不能反结账~");
        }
        checkout.setStatus(Checkout.Status.未结账);
        return checkoutRepository.save(checkout);
    }

    public Boolean checkInit(Long organizationId) {
        return bqf.selectFrom(qCheckout)
                .where(qCheckout.status.eq(Checkout.Status.已结账)
                        .and(qCheckout.organizationId.eq(organizationId))).fetchCount() <= 0;
    }

    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setCheckYear(Integer checkYear) {
            if (checkYear != null) {
                builder.and(qCheckout.checkYear.eq(checkYear));
            }
        }

        public void setCheckMonth(Integer checkMonth) {
            if (checkMonth != null) {
                builder.and(qCheckout.checkMonth.eq(checkMonth));
            }
        }

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
