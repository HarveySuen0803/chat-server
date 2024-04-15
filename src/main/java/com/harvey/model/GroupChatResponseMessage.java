package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {
    private String srcUserName;
    private String content;

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseMessage(String srcUserName, String content) {
        this.srcUserName = srcUserName;
        this.content = content;
    }
    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
