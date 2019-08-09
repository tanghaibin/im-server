package com.haibin.im.server.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * 封装json转换
 *
 * @author haibin.tang
 * @create 2018-07-02 下午3:22
 **/
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> T json2Obj(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    public static <T> T json2Obj(InputStream src, Class<T> clazz) {
        if (src == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(src, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    public static <T> T json2Obj(String content, Class<T> clazzItem, Class... classes) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(clazzItem, classes);
        try {
            return OBJECT_MAPPER.readValue(content, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    public static String obj2Json(Object obj) {
        if(obj == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Json序列化出错", e);
        }
    }
}
