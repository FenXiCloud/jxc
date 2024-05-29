package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountSetsId;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.service.PurchaseOrderService;
import com.flyemu.share.service.RelationOrderVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 生成凭证
 * @创建时间: 2024年05月29日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/relationVoucher")
@RequiredArgsConstructor
@SaCheckLogin
public class RelationOrderVoucherController {
    private final RelationOrderVoucherService relationOrderVoucherService;

    /**
     * 加载列表
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, RelationOrderVoucherService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId){
       query.setMerchantId(merchantId);
       query.setOrganizationId(organizationId);
        return JsonResult.successful(relationOrderVoucherService.query(page,query));
    }
    /**
     * 加载列表
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping("{orderId}")
    public JsonResult loadVoucher(@PathVariable Long orderId, @SaAccountSetsId Long accountSetsId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId){
        return JsonResult.successful(relationOrderVoucherService.loadVoucher(orderId,accountSetsId));
    }

    /**
     * 凭证生成状态
     *
     * @param orderId
     * @param accountDto
     * @return
     */
    @PutMapping("toVoucher/{orderId}")
    public JsonResult to(@PathVariable Long orderId, @SaAccountVal AccountDto accountDto) {
        relationOrderVoucherService.toVoucher(orderId, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(),accountDto.getAccountSetsId());
        return JsonResult.successful();
    }
}
