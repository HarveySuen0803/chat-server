package com.harvey.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceMemoryImpl implements UserService {
    private Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("harvey", "111");
        allUserMap.put("bruce", "111");
        allUserMap.put("jack", "111");
        allUserMap.put("rachel", "111");
    }

    @Override
    public boolean login(String userName, String password) {
        String pass = allUserMap.get(userName);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
