package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupUserJoinResponseMessage extends AbstractResponseMessage {

    public GroupUserJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupUserJoinResponseMessage;
    }
}
