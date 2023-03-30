package com.dx.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 16:31:47
 * @since jdk1.8_202
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "user.auth")
public class AuthProperties {

    /**
     * JWT密钥
     */
    private String secret = "dx123";

    /**
     * 过期时间，默认720（分钟）
     */
    private long expiration = 720;

    /**
     * 用户token刷新时间，默认120（分钟）
     */
    private long refreshTime = 120;
}
