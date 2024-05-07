package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.entity.Menu;
import com.flyemu.share.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @功能描述: 菜单管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 查询商户所有菜单
     *
     * @return
     */
    @GetMapping
    public JsonResult list(@SaMerchantId Long merchantId) {
        return JsonResult.successful(menuService.query(merchantId));
    }

    /**
     * 查询商户授权菜单
     *
     * @return
     */
    @GetMapping("merchant")
    public JsonResult merchantMenu(@SaMerchantId Long merchantId, Menu.MenuGroup menuGroup) {
        return JsonResult.successful(menuService.merchantMenu(merchantId,menuGroup));
    }


}
