package com.dx.request.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 14:02:46
 * @since jdk1.8_202
 */
@Slf4j
public class RequestJsonBodyMethodProcessor extends AbstractMessageConverterMethodArgumentResolver {

    public RequestJsonBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestJsonBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object converters = readWithMessageConverters(webRequest, parameter, parameter.getParameterType());
        RequestJsonBody body = parameter.getParameterAnnotation(RequestJsonBody.class);
        Assert.notNull(body, "annotation is not null");
        String value = Optional.ofNullable(body).map(RequestJsonBody::value).orElse(parameter.getParameterName());
        //通过json转换，convert是一个jsonObject
        Object returnVal = writeJsonObjectToObj((JSONObject) converters, value, parameter);
        if (Optional.ofNullable(body).map(RequestJsonBody::required).orElse(false) && returnVal == null){
            throw new IllegalArgumentException("解析参数失败: " + parameter.getParameterName());
        }
        return returnVal;
    }

    private Object writeJsonObjectToObj(JSONObject jo, String value, MethodParameter parameter){
        Class<?> parameterType = parameter.getParameterType();
        if (jo == null){
            return parameterType.cast(null);
        }
        Object param = jo.get(value);
        //参数为null
        if (Objects.isNull(param)){
            return parameterType.cast(null);
        }
        if (param instanceof JSONObject jsonObject){
            return BeanUtil.toBean(jsonObject, parameterType, CopyOptions.create());
        }
        if (param instanceof JSONArray jsonArray){
            return parameterType.isArray()
                    //数组
                    ? jsonArray.toArray(ArrayUtil.newArray(parameterType.getComponentType(), jsonArray.size()))
                    : BeanUtil.copyToList(jsonArray, parameterType);
        }
        if (parameterType.isArray() && param instanceof String strParam){
            //String数组支持以英文逗号分割
            return StrUtil.splitToArray(strParam, ",");
        }
        return parameterType.cast(param);
    }
}
