package com.dx.request.annotation;

import java.lang.annotation.*;

/**
 * 指示方法参数的注释应该绑定到web请求的主体。请求体通过HttpMessageConverter传递，以根据请求的内容类型解析方法参数。
 * value-作为json参数中的key值
 * required表示是否必须传递
 * 如果注解中不传递vale，则以参数名字作为value
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 13:17:01
 * @since jdk1.8_202
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonBody {

    /**
     * 解析参数的值
     */
    String value() default "";

    /**
     * 是否必传
     */
    boolean required() default true;
}
