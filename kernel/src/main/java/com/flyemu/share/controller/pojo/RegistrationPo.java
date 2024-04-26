package com.flyemu.share.controller.pojo;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * @功能描述: 注册
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
public class RegistrationPo {

    @NotEmpty(message = "商户名称不能为空")
    private String name;

    @NotEmpty(message = "手机号码不能为空")
    private String phone;

    @NotEmpty(message = "联系人不能为空")
    private String linkman;

    @NotEmpty(message = "手机验证码不能为空")
    private String code;
}
