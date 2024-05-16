package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Order;
import com.flyemu.share.form.OrderForm;
import com.flyemu.share.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 销售出库单
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/salesOrder")
@RequiredArgsConstructor
@SaCheckLogin
public class SalesOrderController {
    private final SalesOrderService salesOrderService;

    /**
     * 出库单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list( Page page, SalesOrderService.Query query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(salesOrderService.query(page, query));
    }

    /**
     * 出库单利润列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping("/profit")
    public JsonResult profit( Page page, SalesOrderService.Query query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(salesOrderService.profitList(page, query));
    }

    /**
     * 出库单利润列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping("/rankProducts")
    public JsonResult rankProducts( Page page, SalesOrderService.RankQuery query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(salesOrderService.rankProducts(page, query));
    }

    /**
     * 条件出库单总金额
     *
     * @param merchantId
     * @param query
     * @return
     */
    @GetMapping("/total")
    public JsonResult queryTotal( SalesOrderService.Query query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(salesOrderService.queryTotal(query));
    }

    /**
     * 客户出库单
     * @param customersId
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping("/listBy/{customersId}")
    public JsonResult listBy( @PathVariable Long customersId,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        return JsonResult.successful(salesOrderService.listBy( customersId,merchantId,organizationId));
    }

    /**
     * 保存出库单
     *
     * @param orderForm
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody OrderForm orderForm, @SaAccountVal AccountDto accountDto) {
        salesOrderService.save(orderForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }


    /**
     * 更新出库单状态
     *
     * @param order
     * @param accountDto
     * @return
     */
    @PutMapping
    public JsonResult updateState(@RequestBody Order order, @SaAccountVal AccountDto accountDto) {
        salesOrderService.updateState(order,accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId());
        return JsonResult.successful();
    }


    /**
     * 出库单详情
     *
     * @param merchantId
     * @param orderId
     * @return
     */
    @GetMapping("load/{orderId}")
    public JsonResult load(@SaMerchantId Long merchantId, @PathVariable Long orderId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(salesOrderService.load(merchantId, orderId, organizationId));
    }

    /**
     * 出库单利润详情
     *
     * @param merchantId
     * @param orderId
     * @return
     */
    @GetMapping("loadProfit/{orderId}")
    public JsonResult loadProfit(@SaMerchantId Long merchantId, @PathVariable Long orderId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(salesOrderService.loadProfit(merchantId, orderId, organizationId));
    }


    /**
     * 删除出库单
     *
     * @param orderId
     * @param merchantId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public JsonResult delete(@PathVariable Long orderId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        salesOrderService.delete(orderId, merchantId, organizationId);
        return JsonResult.successful();
    }


}
