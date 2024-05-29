package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
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
     * 入库单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, PurchaseOrderService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(purchaseOrderService.query(page, query));
    }

    /**
     * 条件内入库单总金额
     *
     * @param merchantId
     * @param query
     * @return
     */
    @GetMapping("/total")
    public JsonResult queryTotal(PurchaseOrderService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(purchaseOrderService.queryTotal(query));
    }

    /**
     * 供货商入库单
     *
     * @param merchantId
     * @param vendorsId
     * @return
     */
    @GetMapping("/listBy/{vendorsId}")
    public JsonResult listBy(@PathVariable Long vendorsId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(purchaseOrderService.listBy(vendorsId, merchantId, organizationId));
    }

    /**
     * 保存入库单
     *
     * @param orderForm
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody OrderForm orderForm, @SaAccountVal AccountDto accountDto) {
        Assert.isTrue(orderForm.getOrder().getBillDate().isAfter(accountDto.getCheckDate()),"小于等于结账时间:"+accountDto.getCheckDate()+"不能修改数据");
        purchaseOrderService.save(orderForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }


    /**
     * 更新入库单状态
     *
     * @param order
     * @param accountDto
     * @return
     */
    @PutMapping
    public JsonResult updateState(@RequestBody Order order, @SaAccountVal AccountDto accountDto) {
        purchaseOrderService.updateState(order, accountDto.getMerchantId(), accountDto.getOrganizationId(),accountDto.getCheckDate());
        return JsonResult.successful();
    }


    /**
     * 入库单详情
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
     * 返回创建退单用的list
     *
     * @param merchantId
     * @param orderId
     * @return
     */
    @GetMapping("loadTo/return/{orderId}")
    public JsonResult loadToReturn(@SaMerchantId Long merchantId, @PathVariable Long orderId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(purchaseOrderService.loadToReturn(orderId, merchantId, organizationId));
    }

    /**
     * 删除入库单
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
