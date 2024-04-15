package com.harvey.model;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupUserListResponseMessage extends Message {

    private Set<String> members;

    public GroupUserListResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupUserListResponseMessage;
    }
}
