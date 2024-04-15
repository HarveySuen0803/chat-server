package com.harvey.serializer;

import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Harvey Suen
 */
public class SerializerJsonImpl implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        return JSONUtil.toJsonStr(obj).getBytes(StandardCharsets.UTF_8);
    }
    
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> cls) {
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), cls);
    }
}
