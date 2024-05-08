package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Order;
import com.flyemu.share.form.OrderForm;
import com.flyemu.share.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 采购入库
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/purchaseOrder")
@RequiredArgsConstructor
@SaCheckLogin
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    /**
     * 购货单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list(@SaMerchantId Long merchantId, Page page, PurchaseOrderService.Query query) {
        query.setMerchantId(merchantId);
        return JsonResult.successful(purchaseOrderService.query(page, query));
    }

    /**
     * 条件内购货单总金额
     *
     * @param merchantId
     * @param query
     * @return
     */
    @GetMapping("/total")
    public JsonResult queryTotal(@SaMerchantId Long merchantId, PurchaseOrderService.Query query) {
        query.setMerchantId(merchantId);
        return JsonResult.successful(purchaseOrderService.queryTotal(query));
    }

    /**
     * 供货商购货单
     *
     * @param merchantId
     * @param purchaserId
     * @return
     */
    @GetMapping("/listBy/{purchaserId}")
    public JsonResult listBy(@SaMerchantId Long merchantId, @PathVariable Long purchaserId) {
        return JsonResult.successful(purchaseOrderService.listBy(merchantId, purchaserId));
    }

    /**
     * 保存购货单
     *
     * @param orderForm
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody OrderForm orderForm, @SaAccountVal AccountDto accountDto) {
        purchaseOrderService.save(orderForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }


    /**
     * 更新购货单状态
     *
     * @param order
     * @param accountDto
     * @return
     */
    @PutMapping
    public JsonResult updateState(@RequestBody Order order, @SaAccountVal AccountDto accountDto) {
        purchaseOrderService.updateState(order, accountDto.getMerchantId(), accountDto.getOrganizationId());
        return JsonResult.successful();
    }


    /**
     * 订货单详情
     *
     * @param merchantId
     * @param orderId
     * @return
     */
    @GetMapping("load/{orderId}")
    public JsonResult load(@SaMerchantId Long merchantId, @PathVariable Long orderId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(purchaseOrderService.load(merchantId, orderId, organizationId));
    }


    /**
     * 删除购货单
     *
     * @param orderId
     * @param merchantId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public JsonResult delete(@PathVariable Long orderId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        purchaseOrderService.delete(orderId, merchantId, organizationId);
        return JsonResult.successful();
    }


}
