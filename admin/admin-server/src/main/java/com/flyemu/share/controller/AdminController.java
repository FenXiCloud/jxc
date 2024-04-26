package com.flyemu.share.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.Admin;
import com.flyemu.share.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @功能描述: 管理员管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SaCheckLogin
public class AdminController {

    private final AdminService adminService;


    @GetMapping
    public JsonResult list(Page page, AdminService.Query query) {
        return JsonResult.successful(adminService.query(page, query));
    }

    @PostMapping
    public JsonResult save(@RequestBody @Valid Admin admin) {
        Assert.isNull(admin.getId(), "新增管理员Id必须为空~");
        adminService.save(admin);
        return JsonResult.successful();
    }

    @PutMapping
    public JsonResult update(@RequestBody @Valid Admin admin) {
        Assert.notNull(admin.getId(), "更新管理员Id不允许为空~");
        adminService.save(admin);
        return JsonResult.successful();
    }

    @DeleteMapping("/{adminId}")
    public JsonResult delete(@PathVariable Integer adminId, Integer merchantId) {
        adminService.delete(adminId, merchantId);
        return JsonResult.successful();
    }

    @PutMapping("/reset/password/{adminId}")
    public JsonResult resetPassword(@PathVariable Integer adminId, Integer merchantId) {
        adminService.resetPassword(adminId, merchantId);
        return JsonResult.successful();
    }
}
