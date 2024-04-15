package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupUserJoinRequestMessage extends Message {
    private String groupName;

    private String srcUserName;

    public GroupUserJoinRequestMessage(String srcUserName, String groupName) {
        this.groupName = groupName;
        this.srcUserName = srcUserName;
    }

    @Override
    public int getMessageType() {
        return GroupUserJoinRequestMessage;
    }
}
