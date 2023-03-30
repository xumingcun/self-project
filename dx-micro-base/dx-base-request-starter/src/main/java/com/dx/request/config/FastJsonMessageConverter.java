package com.dx.request.config;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.http.converter.json.AbstractJsonHttpMessageConverter;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * @author dongy
 * @version 1.0
 * @date 2023/3/8 14:07:29
 * @since jdk1.8_202
 */
public class FastJsonMessageConverter extends AbstractJsonHttpMessageConverter {

    @Override
    protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
        //使用fastJson进行解析读取
        JSONReader jsonReader = JSONReader.of(reader);
        return jsonReader.read(JSONObject.class);
    }

    @Override
    protected void writeInternal(Object object, Type type, Writer writer) throws Exception {
        JSONWriter jsonWriter = JSONWriter.ofUTF8();
        jsonWriter.writeAny(object);
    }
}
