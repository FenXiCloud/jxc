package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.entity.ProductsCategory;
import com.flyemu.share.service.ProductsCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 商品分类
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@Slf4j
@RequestMapping("/productsCategory")
@RequiredArgsConstructor
public class ProductsCategoryController {

    private final ProductsCategoryService productsCategoryService;


    @GetMapping
    public JsonResult list(@SaMerchantId Long merchantId, ProductsCategoryService.Query query, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(productsCategoryService.query(query));
    }

    @GetMapping("all")
    public JsonResult listAll(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(productsCategoryService.listAll(merchantId,organizationId));
    }


    @PostMapping
    public JsonResult save(@RequestBody @Valid ProductsCategory productsCategory, @SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        Assert.isNull(productsCategory.getId(), "新增商品分类Id必须为空~");
        productsCategory.setMerchantId(merchantId);
        productsCategory.setOrganizationId(organizationId);
        productsCategoryService.save(productsCategory);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid ProductsCategory productsCategory) {
        Assert.notNull(productsCategory.getId(), "更新商品分类Id不允许为空~");
        productsCategoryService.save(productsCategory);
        return JsonResult.successful();
    }


    @DeleteMapping("/{productsCategoryId}")
    public JsonResult delete(@PathVariable Long productsCategoryId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        productsCategoryService.delete(merchantId, productsCategoryId,organizationId);
        return JsonResult.successful();
    }


    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(productsCategoryService.select(merchantId,organizationId));
    }

}
