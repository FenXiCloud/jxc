package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.enums.OrderType;
import com.flyemu.share.service.OrderReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @功能描述: 销售出库单报表
 * @创建时间: 2024年05月13日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/orderReport")
@RequiredArgsConstructor
@SaCheckLogin
public class OrderReportController {
    private final OrderReportService reportService;

    /**
     * 出库单明细
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping("sales/detail")
    public JsonResult salesDetail(Page page, OrderReportService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        query.setOrderType(Stream.of(OrderType.销售出库单,OrderType.销售退货单).collect(Collectors.toList()));
        query.setOrderStatus(OrderStatus.已审核);
        return JsonResult.successful(reportService.query(page, query));
    }


    /**
     * 出库单汇总
     * 按商品
     *
     * @param accountDto
     * @param page
     * @param summaryQuery
     * @return
     */
    @GetMapping("sales/byProducts")
    public JsonResult salesByProducts(@SaAccountVal AccountDto accountDto, Page page, OrderReportService.SummaryQuery summaryQuery) {
        summaryQuery.setMerchantId(accountDto.getMerchantId());
        summaryQuery.setOrganizationId(accountDto.getOrganizationId());
        summaryQuery.setOrderType(OrderType.销售出库单);
        summaryQuery.setOrderType1(OrderType.销售退货单);
        summaryQuery.setOrderStatus(OrderStatus.已审核);
        return JsonResult.successful(reportService.summaryByProducts(summaryQuery, page));
    }

    /**
     * 入库单明细
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping("purchase/detail")
    public JsonResult purchaseDetail(Page page, OrderReportService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        query.setOrderType(Stream.of(OrderType.采购入库单,OrderType.采购退货单).collect(Collectors.toList()));
        query.setOrderStatus(OrderStatus.已审核);
        return JsonResult.successful(reportService.query(page, query));
    }


    /**
     * 入库单汇总
     * 按商品
     *
     * @param accountDto
     * @param page
     * @param summaryQuery
     * @return
     */
    @GetMapping("purchase/byProducts")
    public JsonResult purchaseByProducts(@SaAccountVal AccountDto accountDto, Page page, OrderReportService.SummaryQuery summaryQuery) {
        summaryQuery.setMerchantId(accountDto.getMerchantId());
        summaryQuery.setOrganizationId(accountDto.getOrganizationId());
        summaryQuery.setOrderType(OrderType.采购入库单);
        summaryQuery.setOrderType1(OrderType.采购退货单);
        summaryQuery.setOrderStatus(OrderStatus.已审核);
        return JsonResult.successful(reportService.summaryByProducts(summaryQuery, page));
    }
}
