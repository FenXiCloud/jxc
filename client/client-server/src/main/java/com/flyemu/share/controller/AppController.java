package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.flyemu.share.serivce.SendSmsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

/**
 * @功能描述: APP
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {


    private final SendSmsService sendSmsService;

    @GetMapping("/")
    public JsonResult index() {
        return JsonResult.successful(LocalDateTime.now());
    }



    /**
     * 发送验证码
     *
     * @param mobile
     * 是否需授权登录 否
     * @return
     */
    @GetMapping("/captcha/sendSmsCaptcha")
    public JsonResult getCode(String mobile, HttpSession session) {
        Assert.isTrue(StrUtil.isNotEmpty(mobile),"请输入手机号~");
        String code = RandomUtil.randomNumbers(6);
        log.info("验证码{}", code);
        sendSmsService.sendSmsCode(mobile, code);
        session.setAttribute(mobile, code);
        return JsonResult.successful();
    }

}
