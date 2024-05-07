package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Vendors;
import com.flyemu.share.service.VendorsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 供货商管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
@SaCheckLogin
public class VendorsController {

    private final VendorsService vendorsService;


    @GetMapping
    public JsonResult list(@SaAccountVal AccountDto accountDto, Page page, VendorsService.Query query) {
        query.setMerchantId(accountDto.getMerchantId());
        return JsonResult.successful(vendorsService.query(page, query));
    }


    @PostMapping
    public JsonResult save(@RequestBody @Valid Vendors vendors, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(vendors.getId(), "新增Id必须为空~");
        vendors.setMerchantId(accountDto.getMerchantId());
        vendors.setOrganizationId(accountDto.getOrganizationId());
        vendorsService.save(vendors);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid Vendors vendors, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(vendors.getId(), "更新Id不允许为空~");
        vendors.setMerchantId(accountDto.getMerchantId());
        vendors.setOrganizationId(accountDto.getOrganizationId());
        vendorsService.save(vendors);
        return JsonResult.successful();
    }


    @DeleteMapping("/{vendorsId}")
    public JsonResult delete(@PathVariable Long vendorsId, @SaMerchantId Long merchantId) {
        vendorsService.delete(vendorsId, merchantId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId) {
        return JsonResult.successful(vendorsService.select(merchantId));
    }
}
