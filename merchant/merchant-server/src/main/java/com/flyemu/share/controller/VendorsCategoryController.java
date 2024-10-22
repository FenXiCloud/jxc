package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.VendorsCategory;
import com.flyemu.share.service.VendorsCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 供货商分类管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/vendorsCategory")
@RequiredArgsConstructor
@SaCheckLogin
public class VendorsCategoryController {

    private final VendorsCategoryService vendorsCategoryService;

    @GetMapping
    public JsonResult list(@SaAccountVal AccountDto accountDto, VendorsCategoryService.Query query) {
        query.setMerchantId(accountDto.getMerchantId());
        query.setOrganizationId(accountDto.getOrganizationId());
        return JsonResult.successful(vendorsCategoryService.query(query));
    }


    @PostMapping
    public JsonResult save(@RequestBody @Valid VendorsCategory vendorsCategory, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(vendorsCategory.getId(), "新增Id必须为空~");
        vendorsCategory.setMerchantId(accountDto.getMerchantId());
        vendorsCategory.setOrganizationId(accountDto.getOrganizationId());
        vendorsCategoryService.save(vendorsCategory);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid VendorsCategory vendorsCategory, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(vendorsCategory.getId(), "更新Id不允许为空~");
        vendorsCategory.setMerchantId(accountDto.getMerchantId());
        vendorsCategory.setOrganizationId(accountDto.getOrganizationId());
        vendorsCategoryService.save(vendorsCategory);
        return JsonResult.successful();
    }


    @DeleteMapping("/{vendorsCategoryId}")
    public JsonResult delete(@PathVariable Long vendorsCategoryId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        vendorsCategoryService.delete(vendorsCategoryId, merchantId,organizationId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(vendorsCategoryService.select(merchantId,organizationId));
    }
}
