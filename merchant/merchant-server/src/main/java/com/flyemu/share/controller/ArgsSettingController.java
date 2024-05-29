package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.service.ArgsSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 系统参数
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/argsSetting")
@RequiredArgsConstructor
@SaCheckLogin
public class ArgsSettingController {
    private final ArgsSettingService settingService;


    /**
     * 更新系统参数
     *
     * @param accountDto
     * @return
     */
    @PutMapping
    public JsonResult update(@RequestBody String costMethod, @SaAccountVal AccountDto accountDto) {
        settingService.update(costMethod, accountDto.getMerchantId(), accountDto.getOrganizationId());

        SaSession session = StpUtil.getTokenSession();
        accountDto.setCostMethod(costMethod);
        session.set(Constants.SESSION_ACCOUNT, accountDto);
        return JsonResult.successful();
    }


    /**
     * 系统参数
     *
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping("load")
    public JsonResult load(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(settingService.load( merchantId, organizationId));
    }

}
