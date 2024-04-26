package com.flyemu.share.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>****************************************************************************</p >
 * <p><b>Copyright © 2010-2024 纷析云（杭州）技有限公司 All Rights Reserved<b></p >
 * <ul style="margin:15px;">
 * <li>Description : com.flyemu.share.config</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2024年01月15日</li>
 * <li>@author     : ____xzy</li>
 * </ul>
 * <p>****************************************************************************</p >
 */
@Getter
@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties("fxy")
public class FxyConfig {

    private String appId;
    private String appSecret;
    private String token;
    private String encodingAesKey;
}