package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.service.SalesOrderReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @功能描述: 销售出库单报表
 * @创建时间: 2024年05月13日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/salesOrder/report")
@RequiredArgsConstructor
@SaCheckLogin
public class SalesOrderReportController {
    private final SalesOrderReportService reportService;

    /**
     * 出库单明细
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, SalesOrderReportService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(reportService.query(page, query));
    }

}
