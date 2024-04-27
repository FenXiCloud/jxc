package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.Customers;
import com.flyemu.share.entity.Merchant;
import com.flyemu.share.service.CustomersService;
import com.flyemu.share.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 客户管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

    private final CustomersService customersService;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @GetMapping
    public JsonResult list(Page page) {
        return JsonResult.successful(customersService.query(page));
    }

    /**
     * 新增商户
     *
     * @param customers
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid Customers customers) {
        Assert.isNull(customers.getId(), "新增商户Id必须为空~");
        customersService.save(customers);
        return JsonResult.successful();
    }

    /**
     * 更新商户
     *
     * @param customers
     * @return
     */
    @PutMapping
    public JsonResult update(@RequestBody @Valid Customers customers) {
        Assert.notNull(customers.getId(), "更新商户Id不允许为空~");
        customersService.save(customers);
        return JsonResult.successful();
    }

    /**
     * 删除商户
     *
     * @param customersId
     * @return
     */
    @DeleteMapping("/{customersId}")
    public JsonResult delete(@PathVariable Integer customersId) {
        customersService.delete(customersId);
        return JsonResult.successful();
    }

}
