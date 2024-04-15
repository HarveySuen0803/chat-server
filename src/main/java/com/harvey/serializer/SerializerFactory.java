package com.harvey.serializer;

import com.harvey.service.UserService;
import com.harvey.service.UserServiceMemoryImpl;

/**
 * @author Harvey Suen
 */
public class SerializerFactory {
    private static Serializer serializer = new SerializerJavaImpl();
    
    public static Serializer getSerializer() {
        return serializer;
    }
}
