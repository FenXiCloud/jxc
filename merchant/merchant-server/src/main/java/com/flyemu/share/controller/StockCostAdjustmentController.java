package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.form.StockCostAdjustmentForm;
import com.flyemu.share.service.StockCostAdjustmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 成本调整
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/stockAdjustment")
@RequiredArgsConstructor
@SaCheckLogin
public class StockCostAdjustmentController {
    private final StockCostAdjustmentService adjustmentService;

    /**
     * 成本调整单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, StockCostAdjustmentService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(adjustmentService.query(page, query));
    }



    /**
     * 保存成本调整单
     *
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody StockCostAdjustmentForm adjustmentForm, @SaAccountVal AccountDto accountDto) {
        adjustmentService.save(adjustmentForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }



    /**
     * 成本调整单详情
     *
     * @param merchantId
     * @param adjustmentId
     * @return
     */
    @GetMapping("load/{adjustmentId}")
    public JsonResult load(@SaMerchantId Long merchantId, @PathVariable Long adjustmentId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(adjustmentService.load( adjustmentId,merchantId, organizationId));
    }

}
