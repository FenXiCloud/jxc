package com.flyemu.share.serivce;


import com.flyemu.share.aliyuncs.SmsService;
import com.flyemu.share.config.AppConfig;
import com.flyemu.share.service.AbsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SendSmsService extends AbsService {

    private final AppConfig appConfig;

    private final Environment environment;

    private final SmsService smsService;

    public void sendSmsCode(String mobile, String code) {
        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            SmsService.SmsBody smsBody = new SmsService.SmsBody(mobile, appConfig.getAliyun().getSms().getSignature(), appConfig.getAliyun().getSms().getTemplateCode().getVerification());
            Map<String, String> params = new HashMap<>(1);
            params.put("code", code);
            smsBody.setTemplateParam(params);
            smsService.send(smsBody);
        } else {
            log.info("\n 验证码?:{}", code);
        }
    }

}
