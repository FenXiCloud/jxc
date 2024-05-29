package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.api.Fenxi;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.PurchaserOrderDto;
import com.flyemu.share.dto.VoucherDetailsDto;
import com.flyemu.share.dto.VoucherDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.enums.OrderType;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.OrderDetailRepository;
import com.flyemu.share.repository.OrderRepository;
import com.flyemu.share.repository.RelationOrderVoucherRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @功能描述: 凭证
 * @创建时间: 2024年05月29日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationOrderVoucherService extends AbsService {
    private final static QRelationSubject qRelationSubject = QRelationSubject.relationSubject;
    private final Fenxi fenxi;
    private final static QOrder qOrder = QOrder.order;
    private final static QRelationOrderVoucher qRelationOrderVoucher = QRelationOrderVoucher.relationOrderVoucher;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final static QCustomers qCustomers = QCustomers.customers;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QStockItem qStockItem = QStockItem.stockItem;
    private final RelationOrderVoucherRepository voucherRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CodeSeedService codeSeedService;
    private final StockItemService stockItemService;

    public PageResults<Dict> query(Page page, Query query) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("merchantId", query.getMerchantId());
        params.put("organizationId", query.getOrganizationId());
        params.put("orderStatus", OrderStatus.已审核.name());
        if (null != query.start && null != query.end) {
            params.put("start", query.getStart());
            params.put("end", query.getEnd());
        }
        if (StrUtil.isNotEmpty(query.filter)) {
            params.put("filter", query.filter);
        }

        org.sagacity.sqltoy.model.Page sqlPage = new org.sagacity.sqltoy.model.Page(page.getSize(), page.getPage());
        org.sagacity.sqltoy.model.Page<Dict> summaryPage = lazyDao.findPageBySql(sqlPage, "relationOrderVoucher", params, Dict.class);
        return new PageResults<>(summaryPage.getRows(), page, summaryPage.getRecordCount());
    }

    public JSONObject loadVoucher(Long orderId, Long accountSetsId) {
       Long voucherId = bqf.selectFrom(qRelationOrderVoucher)
                .select(qRelationOrderVoucher.voucherId)
                .where(qRelationOrderVoucher.orderId.eq(orderId)).fetchFirst();

       JSONObject jsonObject =  fenxi.loadVoucher(accountSetsId, voucherId);
        if (jsonObject == null) {

        }
       return jsonObject;
    }

    @Transactional
    public void toVoucher(Long orderId, Long adminId, Long merchantId, Long organizationId, Long accountSetsId) {
        Assert.isFalse(bqf.selectFrom(qRelationOrderVoucher).where(qRelationOrderVoucher.orderId.eq(orderId).and(qRelationOrderVoucher.merchantId.eq(merchantId)
                .and(qRelationOrderVoucher.organizationId.eq(organizationId)))).fetchCount() > 0, "该订单已经生成凭证，请勿重复操作");
        Map<String, RelationSubject> subjectMap = new HashMap<>();
        bqf.selectFrom(qRelationSubject).where(qRelationSubject.merchantId.eq(merchantId).and(qRelationSubject.organizationId.eq(organizationId))).fetch().forEach(tuple -> {
            subjectMap.put(tuple.getName(), tuple);
        });
        Tuple tuple = bqf.selectFrom(qOrder)
                .select(qOrder.id, qOrder.code, qOrder.billDate, qOrder.orderType, qOrder.discountedAmount, qCustomers.name)
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .where(qOrder.id.eq(orderId).and(qOrder.merchantId.eq(merchantId)).and(qOrder.organizationId.eq(organizationId)))
                .fetchFirst();
        Dict order = new Dict()
                .set("id", tuple.get(qOrder.id))
                .set("code", tuple.get(qOrder.code))
                .set("billDate", tuple.get(qOrder.billDate))
                .set("orderType", tuple.get(qOrder.orderType))
                .set("customerName", tuple.get(qCustomers.name))
                .set("discountedAmount", tuple.get(qOrder.discountedAmount));

        VoucherDto entity = new VoucherDto();
//        entity.setVoucherDate(tuple.get(qOrder.billDate));
        entity.setWord("记");
        entity.setCode(2);
        entity.setCarryForward(false);
        entity.setRemark("备注内容");
        entity.setLocked(false);
        entity.setReceiptNum(0);

        List<VoucherDetailsDto> details = new ArrayList<>();
        RelationSubject cSubject = subjectMap.get("应收账款");
        RelationSubject dSubject = subjectMap.get("库存商品");
        VoucherDetailsDto cDetailsDto = new VoucherDetailsDto();
        cDetailsDto.setCreditAmountFor(BigDecimal.ZERO);
        cDetailsDto.setSubjectId(cSubject.getSubjectId());
        cDetailsDto.setSummary(tuple.get(qOrder.orderType) + ":" + tuple.get(qOrder.code));
        cDetailsDto.setCreditAmount(tuple.get(qOrder.discountedAmount));
        details.add(cDetailsDto);

        VoucherDetailsDto dDetailsDto = new VoucherDetailsDto();
        dDetailsDto.setCreditAmountFor(BigDecimal.ZERO);
        dDetailsDto.setSubjectId(dSubject.getSubjectId());
        dDetailsDto.setSummary(tuple.get(qOrder.orderType) + ":" + tuple.get(qOrder.code));
        dDetailsDto.setDebitAmount(tuple.get(qOrder.discountedAmount));
        details.add(dDetailsDto);
        entity.setDetails(details);
        JSONObject jsonObject = fenxi.createVoucher(accountSetsId, entity);
        if (jsonObject != null) {
            RelationOrderVoucher voucher = new RelationOrderVoucher();
            voucher.setAdminId(adminId);
            voucher.setOrderId(orderId);
            voucher.setMerchantId(merchantId);
            voucher.setOrganizationId(organizationId);
            voucher.setAccountSetsId(accountSetsId);
            voucher.setVoucherId(Long.valueOf(jsonObject.getString("id")));
            voucher.setWord(jsonObject.getString("word"));
            voucher.setCode(jsonObject.getInteger("code"));
            voucher.setVoucherDate(jsonObject.getObject("voucherDate", LocalDate.class));
            voucherRepository.save(voucher);
        } else {
            throw new ServiceException("没有返回");
        }
    }


    @Data
    public static class Query {
        private Long merchantId;
        private Long organizationId;
        private LocalDate start;
        private LocalDate end;
        private String filter;
        private String orderStatus;
    }
}
