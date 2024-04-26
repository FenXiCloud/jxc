package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @功能描述: 公众号设置
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WechatSettingService extends AbsService {

    private final WechatSettingRepository wechatSettingRepository;

    private final CodeSeedService codeSeedService;

    @Transactional
    public WechatSetting save(WechatSetting wechatSetting) {
        if (wechatSetting.getId() != null) {
            //更新
            WechatSetting original = wechatSettingRepository.getById(wechatSetting.getId());
            BeanUtil.copyProperties(wechatSetting, original, CopyOptions.create().ignoreNullValue());
            return wechatSettingRepository.save(original);
        }
        WechatSetting m = wechatSettingRepository.save(wechatSetting);
        return m;
    }

}
