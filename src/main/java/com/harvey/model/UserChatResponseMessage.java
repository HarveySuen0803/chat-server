package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserChatResponseMessage extends AbstractResponseMessage {

    private String srcUserName;
    private String content;

    public UserChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public UserChatResponseMessage(String srcUserName, String content) {
        this.srcUserName = srcUserName;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return UserChatResponseMessage;
    }
}
