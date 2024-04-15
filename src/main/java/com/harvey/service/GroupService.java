package com.harvey.service;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

public interface GroupService {
    boolean addGroup(String groupName, Set<String> userNameSet);

    boolean delGroup(String groupName);
    
    boolean addUser(String groupName, String userName);

    boolean delUser(String groupName, String userName);

    Set<String> getUserNameList(String groupName);

    List<Channel> getUserChannelList(String groupName);
}
