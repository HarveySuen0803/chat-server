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
        String s = new String(bytes, StandardCharsets.UTF_8);
        
        System.out.println("-----------------------------------------------------");
        System.out.println(s);
        System.out.println(JSONUtil.toBean(s, cls));
        System.out.println("-----------------------------------------------------");
        
        
        return JSONUtil.toBean(s, cls);
    }
}
