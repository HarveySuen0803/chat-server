package com.harvey.service;

public abstract class GroupServiceFactory {

    private static GroupService groupService = new GroupServiceMemoryImpl();

    public static GroupService getGroupService() {
        return groupService;
    }
}
