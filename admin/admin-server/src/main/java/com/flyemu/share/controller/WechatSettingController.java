package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.Merchant;
import com.flyemu.share.entity.WechatSetting;
import com.flyemu.share.service.MerchantService;
import com.flyemu.share.service.WechatSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 商户管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/wechat/setting")
@RequiredArgsConstructor
public class WechatSettingController {

    private final WechatSettingService wechatSettingService;


    @PostMapping
    public JsonResult save(@RequestBody @Valid WechatSetting wechatSetting) {
        wechatSettingService.save(wechatSetting);
        return JsonResult.successful();
    }

    @PutMapping
    public JsonResult update(@RequestBody @Valid WechatSetting wechatSetting) {
        wechatSettingService.save(wechatSetting);
        return JsonResult.successful();
    }


}
