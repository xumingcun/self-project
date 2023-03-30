package com.dx.request.annotation;

import java.lang.annotation.*;

/**
 * 约束字段值不为空
 * 此约束情况只在执行{{}}方法是执行。
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 13:06:51
 * @since jdk1.8_202
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {

    String message() default "must not be empty";

    Class<?>[] group() default {};

}
