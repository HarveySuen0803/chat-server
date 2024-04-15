package com.harvey.service;

import com.harvey.model.Group;
import com.harvey.session.SessionFactory;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GroupServiceMemoryImpl implements GroupService {
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    @Override
    public boolean addGroup(String groupName, Set<String> userNameSet) {
        if (groupMap.containsKey(groupName)) {
            return false;
        }
        
        groupMap.put(groupName, new Group(groupName, userNameSet));
        
        return true;
    }
    
    @Override
    public boolean delGroup(String groupName) {
        if (!groupMap.containsKey(groupName)) {
            return false;
        }
        
        groupMap.remove(groupName);
        
        return true;
    }
    
    @Override
    public boolean addUser(String groupName, String userName) {
        Group group = groupMap.get(groupName);
        if (group == null) {
            return false;
        }
        
        group.getUserNameSet().add(userName);
        
        return true;
    }

    @Override
    public boolean delUser(String groupName, String userName) {
        Group group = groupMap.get(groupName);
        if (group == null) {
            return false;
        }
        
        group.getUserNameSet().remove(userName);
        
        return true;
    }
    
    @Override
    public Set<String> getUserNameList(String groupName) {
        return groupMap.getOrDefault(groupName, Group.EMPTY_GROUP).getUserNameSet();
    }

    @Override
    public List<Channel> getUserChannelList(String groupName) {
        return getUserNameList(groupName).stream()
                .map(member -> SessionFactory.getSession().getChannel(member))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
