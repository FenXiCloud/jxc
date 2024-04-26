package com.flyemu.share.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>****************************************************************************</p >
 * <p><b>Copyright © 2010-2024 纷析云（杭州）技有限公司 All Rights Reserved<b></p >
 * <ul style="margin:15px;">
 * <li>Description : com.flyemu.share.config</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2024年01月10日</li>
 * <li>@author     : ____xzy</li>
 * </ul>
 * <p>****************************************************************************</p >
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String subAppId;

    private String subMchId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户V3密钥
     */
    private String apiV3Key;

    /**
     * 证书号
     */
    private String certSerialNo;
    private String keyPath;

    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String privateKeyPath;

    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String privateCertPath;

    /**
     * 回调地址
     */
    private String notifyUrl;

}