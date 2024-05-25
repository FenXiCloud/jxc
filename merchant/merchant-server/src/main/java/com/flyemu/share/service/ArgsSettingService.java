package com.flyemu.share.service;

import cn.hutool.core.lang.Dict;
import com.flyemu.share.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @功能描述: 参数设置
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArgsSettingService extends AbsService {
    private final static QArgsSetting qArgsSetting = QArgsSetting.argsSetting;

    /**
     * 更新系统参数
     * @param costMethod
     * @param merchantId
     * @param organizationId
     */
    @Transactional
    public void update(String costMethod, Long merchantId, Long organizationId) {
        jqf.update(qArgsSetting)
                .set(qArgsSetting.costMethod,costMethod)
                .where(qArgsSetting.merchantId.eq(merchantId).and(qArgsSetting.organizationId.eq(organizationId))).execute();
    }

    /**
     * 系统参数详情
     * @param merchantId
     * @param organizationId
     * @return
     */
    public Dict load(Long merchantId, Long organizationId) {
       ArgsSetting argsSetting = bqf.selectFrom(qArgsSetting)
                .where(qArgsSetting.merchantId.eq(merchantId).and(qArgsSetting.organizationId.eq(organizationId))).fetchFirst();
        return Dict.create().set("costMethod", argsSetting.getCostMethod());
    }


}

