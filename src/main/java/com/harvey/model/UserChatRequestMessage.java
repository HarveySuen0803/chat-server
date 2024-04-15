package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserChatRequestMessage extends Message {
    private String content;
    private String tarUserName;
    private String srcUserName;

    public UserChatRequestMessage() {
    }

    public UserChatRequestMessage(String src, String tarUserName, String content) {
        this.srcUserName = src;
        this.tarUserName = tarUserName;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return UserChatRequestMessage;
    }
}
