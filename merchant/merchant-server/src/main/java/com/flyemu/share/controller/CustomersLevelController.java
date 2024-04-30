package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.CustomersLevel;
import com.flyemu.share.service.CustomersLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 客户级别管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/customersLevel")
@RequiredArgsConstructor
@SaCheckLogin
public class CustomersLevelController {

    private final CustomersLevelService customersLevelService;

    @GetMapping
    public JsonResult list(@SaAccountVal AccountDto accountDto, CustomersLevelService.Query query) {
        query.setMerchantId(accountDto.getMerchantId());
        return JsonResult.successful(customersLevelService.query(query));
    }



    @PostMapping
    public JsonResult save(@RequestBody @Valid CustomersLevel customersLevel, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(customersLevel.getId(), "新增Id必须为空~");
        customersLevel.setMerchantId(accountDto.getMerchantId());
        customersLevelService.save(customersLevel);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid CustomersLevel customersLevel, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(customersLevel.getId(), "更新Id不允许为空~");
        customersLevel.setMerchantId(accountDto.getMerchantId());
        customersLevelService.save(customersLevel);
        return JsonResult.successful();
    }


    @DeleteMapping("/{customersLevelId}")
    public JsonResult delete(@PathVariable Integer customersLevelId, @SaMerchantId Integer merchantId) {
        customersLevelService.delete(customersLevelId, merchantId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Integer merchantId) {
        return JsonResult.successful(customersLevelService.select(merchantId));
    }
}
