package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupUserListRequestMessage extends Message {
    private String groupName;

    public GroupUserListRequestMessage(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupUserListRequestMessage;
    }
}
