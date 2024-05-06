package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.CustomersCategory;
import com.flyemu.share.service.CustomersCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 客户分类管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/customersCategory")
@RequiredArgsConstructor
@SaCheckLogin
public class CustomersCategoryController {

    private final CustomersCategoryService customersCategoryService;

    @GetMapping
    public JsonResult list(@SaAccountVal AccountDto accountDto, CustomersCategoryService.Query query) {
        query.setMerchantId(accountDto.getMerchantId());
        query.setOrganizationId(accountDto.getOrganizationId());
        return JsonResult.successful(customersCategoryService.query(query));
    }



    @PostMapping
    public JsonResult save(@RequestBody @Valid CustomersCategory customersCategory, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(customersCategory.getId(), "新增Id必须为空~");
        customersCategory.setMerchantId(accountDto.getMerchantId());
        customersCategory.setOrganizationId(accountDto.getOrganizationId());
        customersCategoryService.save(customersCategory);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid CustomersCategory customersCategory, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(customersCategory.getId(), "更新Id不允许为空~");
        customersCategory.setMerchantId(accountDto.getMerchantId());
        customersCategory.setOrganizationId(accountDto.getOrganizationId());
        customersCategoryService.save(customersCategory);
        return JsonResult.successful();
    }

    @DeleteMapping("/{customersCategoryId}")
    public JsonResult delete(@PathVariable Integer customersCategoryId, @SaMerchantId Integer merchantId, @SaOrganizationId Integer organizationId) {
        customersCategoryService.delete(customersCategoryId, merchantId,organizationId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Integer merchantId, @SaOrganizationId Integer organizationId) {
        return JsonResult.successful(customersCategoryService.select(merchantId,organizationId));
    }
}
