package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.entity.Products;
import com.flyemu.share.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



/**
 * @功能描述: 商品列表
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@SaCheckLogin
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping
    public JsonResult list( Page page, ProductsService.Query query,@SaMerchantId Integer merchantId,@SaOrganizationId Integer organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(productsService.query(page, query));
    }

    @PostMapping
    public JsonResult save(@RequestBody Products products, @SaMerchantId Integer merchantId, @SaOrganizationId Integer organizationId) {
//        productsService.save(products, merchantId);
        return JsonResult.successful();
    }


    @GetMapping("/load/{productsId}")
    public JsonResult load(@PathVariable Long productsId, @SaMerchantId Integer merchantId) {
        return JsonResult.successful(productsService.load(productsId, merchantId));
    }


    @DeleteMapping("/{productsId}")
    public JsonResult delete(@PathVariable Long productsId, @SaMerchantId Integer merchantId) {
        productsService.delete(productsId, merchantId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Integer merchantId) {
        return JsonResult.successful(productsService.select(merchantId));
    }

}
