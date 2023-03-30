package com.dx.request.config;

import com.dx.request.annotation.RequestJsonBodyMethodProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 14:00:11
 * @since jdk1.8_202
 */
@Configuration
public class WebRequestJsonConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        List<HttpMessageConverter<?>> resolverList = new ArrayList<>();
        resolverList.add(new FastJsonMessageConverter());
        resolvers.add(new RequestJsonBodyMethodProcessor(resolverList));
    }

}
