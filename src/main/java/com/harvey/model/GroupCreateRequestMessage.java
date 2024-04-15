package com.harvey.model;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message {
    private String groupName;
    private Set<String> userNameSet;

    public GroupCreateRequestMessage(String groupName, Set<String> userNameSet) {
        this.groupName = groupName;
        this.userNameSet = userNameSet;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
