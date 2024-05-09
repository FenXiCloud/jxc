package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Customers;
import com.flyemu.share.service.CustomersService;
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
    public JsonResult list(Page page,CustomersService.Query query, @SaOrganizationId Long organizationId, @SaMerchantId Long merchantId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(customersService.query(page,query));
    }

    /**
     * 新增商户
     *
     * @param customers
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid Customers customers, @SaOrganizationId Long organizationId, @SaMerchantId Long merchantId, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(customers.getId(), "新增商户Id必须为空~");
        customers.setMerchantId(merchantId);
        customers.setOrganizationId(organizationId);
        customersService.save(customers,accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }

    /**
     * 更新商户
     *
     * @param customers
     * @return
     */
    @PutMapping
    public JsonResult update(@RequestBody @Valid Customers customers, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(customers.getId(), "更新商户Id不允许为空~");
        customersService.save(customers,accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }

    /**
     * 删除商户
     *
     * @param customersId
     * @return
     */
    @DeleteMapping("/{customersId}")
    public JsonResult delete(@PathVariable Long customersId, @SaOrganizationId Long organizationId, @SaMerchantId Long merchantId) {
        customersService.delete(customersId,merchantId,organizationId);
        return JsonResult.successful();
    }


    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId,@SaOrganizationId Long organizationId) {
        return JsonResult.successful(customersService.select(merchantId,organizationId));
    }

}
