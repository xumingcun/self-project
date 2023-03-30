package com.dx.management.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dx.management.config.AuthProperties;
import com.dx.management.config.IgnoreWhiteProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 认证拦截
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 16:30:17
 * @since jdk1.8_202
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Resource
    private IgnoreWhiteProperties whiteProperties;

    @Resource
    private AuthProperties authProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        //对外开放的endpoints
        if (matches(url, whiteProperties.getEndPoint())){
            return chain.filter(exchange);
        }
        //白名单过滤，跳过不需要验证的路径
        if (matches(url, whiteProperties.getWhites())){
            return chain.filter(exchange);
        }
        //获取认证
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
       /* addHeader(mutate, SecurityConstants.DETAILS_USER_TOKEN,userToken);
        addHeader(mutate,SecurityConstants.DETAILS_USER_ID,userId);
        addHeader(mutate,SecurityConstants.DETAILS_USERNAME,userName);*/

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }

    private boolean matches(String str, List<String> strings){
        if (StrUtil.isEmpty(str) || CollUtil.isEmpty(strings)){
            return false;
        }
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : strings) {
            if (matcher.match(pattern, str)){
                return true;
            }
        }
        return false;
    }

    public String getAuthorization(ServerHttpRequest request){
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 如果前端设置了令牌前缀，则裁剪掉前缀
        /*if (StringUtils.isNotEmpty(authentication) && authentication.startsWith(TokenConstants.PREFIX)) {
            authentication = authentication.replaceFirst(TokenConstants.PREFIX, "");
        }*/

        return authentication;
    }


    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        mutate.header(name, valueStr);
    }
}
