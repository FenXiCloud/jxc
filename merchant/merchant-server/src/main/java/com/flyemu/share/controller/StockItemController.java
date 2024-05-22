package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.enums.OrderType;
import com.flyemu.share.service.OrderReportService;
import com.flyemu.share.service.StockItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @功能描述: 库存明细表
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/stockItem")
@RequiredArgsConstructor
@SaCheckLogin
public class StockItemController {
    private final StockItemService stockItemService;

    /**
     * 仓库出入明细
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping("detail")
    public JsonResult detail(Page page, StockItemService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(stockItemService.query(page, query));
    }

    /**
     * 仓库出入明细
     *
     * @param accountDto
     * @param query
     * @return
     */
    @GetMapping("adjustment/detail")
    public JsonResult adjustment( StockItemService.Query query,@SaAccountVal AccountDto accountDto) {
        query.setMerchantId(accountDto.getMerchantId());
        query.setOrganizationId(accountDto.getOrganizationId());
        return JsonResult.successful(stockItemService.adjustment( query,accountDto.getCheckDate()));
    }


    /**
     * 仓库明细汇总
     * 按商品
     *
     * @param accountDto
     * @param page
     * @param stockQuery
     * @return
     */
    @GetMapping("byProducts")
    public JsonResult salesByProducts(@SaAccountVal AccountDto accountDto, Page page, StockItemService.StockQuery stockQuery) {
        stockQuery.setMerchantId(accountDto.getMerchantId());
        stockQuery.setOrganizationId(accountDto.getOrganizationId());
        return JsonResult.successful(stockItemService.stockByProducts(stockQuery, page));
    }
}
