package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupUserQuitResponseMessage extends AbstractResponseMessage {
    public GroupUserQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupUserQuitResponseMessage;
    }
}
