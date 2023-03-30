package com.dx.management.constant;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/7 16:59:41
 * @since jdk1.8_202
 */
public class TokenConstant {

    /**
     * redis登录key
     */
    public static final String LOGIN_TOKEN_KEY = "user:login:key:";

    /**
     * 失效的key
     */
    public static final String INVALID_LOGIN_TOKEN_KEY = "invalid:login:token:key";

    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";
}
