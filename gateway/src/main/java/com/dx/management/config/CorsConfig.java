package com.dx.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 15:45:54
 * @since jdk1.8_202
 */
public class CorsConfig {

    @Bean
    public WebFilter corsFilter(){
        return (serverWebExchange, webFilterChain) -> {
            ServerHttpRequest request = serverWebExchange.getRequest();
            HttpHeaders requestHeaders = request.getHeaders();
            ServerHttpResponse response = serverWebExchange.getResponse();
            HttpMethod method = requestHeaders.getAccessControlRequestMethod();
            HttpHeaders headers = response.getHeaders();
            //添加跨域配置
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
            headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlAllowHeaders());
            if (Objects.nonNull(method)){
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, method.name());
            }
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
            //如果是预检请求，直接返回OK
            if (Objects.equals(method, HttpMethod.OPTIONS)){
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return webFilterChain.filter(serverWebExchange);
        };
    }
}
