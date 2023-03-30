package com.dx.management.config;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 白名单配置
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 16:23:02
 * @since jdk1.8_202
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteProperties {

    /**
     * 放行的白名单列表，网关不校验此处的路径
     */
    private List<String> whites = Collections.emptyList();

    private final List<String> endPoint = ListUtil.of(
            //监控
            "/*/actuator/.*"
    );

}
