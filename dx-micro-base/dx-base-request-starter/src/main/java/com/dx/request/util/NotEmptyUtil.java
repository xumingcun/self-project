package com.dx.request.util;


import cn.hutool.core.lang.Assert;
import com.dx.request.annotation.NotEmpty;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 判断类中的字段工具类
 * 以注解为判断依据 {{@link com.dx.request.annotation.NotEmpty}}
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 13:20:48
 * @since jdk1.8_202
 */
public class NotEmptyUtil {

    /**
     * 判定数据中是否为空
     * @param data 数据
     * @param <T> 泛型
     */
    public static  <T>  void  sentence(T data){
        Assert.notNull(data, "object requires not null");
        Class<?> clz = data.getClass();
        //只对带有注解的字段进行判定
        try {
            //获取属性
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                Field field = clz.getDeclaredField(descriptor.getName());
                if (!field.isAnnotationPresent(NotEmpty.class)){
                    //没有注解进行下一个校验
                    continue;
                }
                NotEmpty annotation = field.getAnnotation(NotEmpty.class);
                //进行判定,目前只有信息判定
                Method readMethod = descriptor.getReadMethod();
                Assert.notNull(readMethod, "Field " + descriptor.getName() + " has not getter method");
                notEmpty(readMethod.invoke(data), annotation.message());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据判空
     * @param data 数据
     * @param message 错误信息提示
     * @param <T> 数据格式
     */
    public static <T> void notEmpty(T data, String message){
        //空指针
        Assert.notNull(data, message);
        //迭代器
        if (data instanceof Iterable<?> iterData){
            Assert.notEmpty(iterData, message);
        }
        //map
        if (data instanceof Map<?,?> mapData){
            Assert.notEmpty(mapData, message);
        }
        //字符或字符串
        if (data instanceof CharSequence charData){
            Assert.notEmpty(charData, message);
        }
        //数组
        if (data instanceof Object[] arrayData){
            Assert.notNull(arrayData, message);
        }
    }

}
