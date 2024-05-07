package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Units;
import com.flyemu.share.service.UnitsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 单位
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
@SaCheckLogin
public class UnitsController {

    private final UnitsService unitsService;

    @GetMapping
    public JsonResult list(@SaAccountVal AccountDto accountDto, UnitsService.Query query) {
        query.setMerchantId(accountDto.getMerchantId());
        query.setOrganizationId(accountDto.getOrganizationId());
        return JsonResult.successful(unitsService.query(query));
    }

    @PostMapping
    public JsonResult save(@RequestBody @Valid Units units, @SaAccountVal AccountDto accountDto) {
        Assert.isNull(units.getId(), "新增Id必须为空~");
        units.setMerchantId(accountDto.getMerchantId());
        units.setOrganizationId(accountDto.getOrganizationId());
        unitsService.save(units);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid Units units, @SaAccountVal AccountDto accountDto) {
        Assert.notNull(units.getId(), "更新Id不允许为空~");
        units.setMerchantId(accountDto.getMerchantId());
        units.setOrganizationId(accountDto.getOrganizationId());
        unitsService.save(units);
        return JsonResult.successful();
    }


    @DeleteMapping("/{unitsId}")
    public JsonResult delete(@PathVariable Long unitsId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        unitsService.delete(unitsId, merchantId,organizationId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(unitsService.select(merchantId,organizationId));
    }
}
