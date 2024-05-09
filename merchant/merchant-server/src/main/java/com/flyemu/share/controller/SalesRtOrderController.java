package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Order;
import com.flyemu.share.form.OrderForm;
import com.flyemu.share.service.SalesRtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 销售退货单
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/salesRtOrder")
@RequiredArgsConstructor
@SaCheckLogin
public class SalesRtOrderController {
    private final SalesRtOrderService rtOrderService;

    /**
     * 退货单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list( Page page, SalesRtOrderService.Query query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(rtOrderService.query(page, query));
    }

    /**
     * 条件退货单总金额
     *
     * @param merchantId
     * @param query
     * @return
     */
    @GetMapping("/total")
    public JsonResult queryTotal( SalesRtOrderService.Query query,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(rtOrderService.queryTotal(query));
    }

    /**
     * 客户退货单
     * @param customersId
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping("/listBy/{customersId}")
    public JsonResult listBy( @PathVariable Long customersId,@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        return JsonResult.successful(rtOrderService.listBy( customersId,merchantId,organizationId));
    }

    /**
     * 保存退货单
     *
     * @param orderForm
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody OrderForm orderForm, @SaAccountVal AccountDto accountDto) {
        rtOrderService.save(orderForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }


    /**
     * 更新退货单状态
     *
     * @param order
     * @param accountDto
     * @return
     */
    @PutMapping
    public JsonResult updateState(@RequestBody Order order, @SaAccountVal AccountDto accountDto) {
        rtOrderService.updateState(order, accountDto.getMerchantId(), accountDto.getOrganizationId());
        return JsonResult.successful();
    }


    /**
     * 退货单详情
     *
     * @param merchantId
     * @param orderId
     * @return
     */
    @GetMapping("load/{orderId}")
    public JsonResult load(@SaMerchantId Long merchantId, @PathVariable Long orderId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(rtOrderService.load(merchantId, orderId, organizationId));
    }


    /**
     * 删除退货单
     *
     * @param orderId
     * @param merchantId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public JsonResult delete(@PathVariable Long orderId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        rtOrderService.delete(orderId, merchantId, organizationId);
        return JsonResult.successful();
    }


}
