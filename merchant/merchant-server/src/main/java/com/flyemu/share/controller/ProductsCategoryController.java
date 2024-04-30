package com.flyemu.share.controller;


import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.entity.ProductsCategory;
import com.flyemu.share.service.ProductsCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * @功能描述: 商品分类
 * @创建时间: 2023年08月08日
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
    public JsonResult list(@SaMerchantId Integer merchantId, ProductsCategoryService.Query query) {
        query.setMerchantId(merchantId);
        return JsonResult.successful(productsCategoryService.query(query));
    }

    @GetMapping("all")
    public JsonResult listAll(@SaMerchantId Integer merchantId) {
        return JsonResult.successful(productsCategoryService.listAll(merchantId));
    }


    @PostMapping
    public JsonResult save(@RequestBody @Valid ProductsCategory productsCategory, @SaMerchantId Integer merchantId) {
        Assert.isNull(productsCategory.getId(), "新增商品分类Id必须为空~");
        productsCategory.setMerchantId(merchantId);
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
    public JsonResult delete(@PathVariable Integer productsCategoryId, @SaMerchantId Integer merchantId) {
        productsCategoryService.delete(merchantId, productsCategoryId);
        return JsonResult.successful();
    }


    @GetMapping("select")
    public JsonResult select(@SaMerchantId Integer merchantId) {
        return JsonResult.successful(productsCategoryService.select(merchantId));
    }

}
