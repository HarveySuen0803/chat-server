package com.harvey.model;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class Group {
    private String name;
    
    private Set<String> userNameSet;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> userNameSet) {
        this.name = name;
        this.userNameSet = userNameSet;
    }
}
