package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatRequestMessage extends Message {
    private String content;
    private String groupName;
    private String srcUserName;

    public GroupChatRequestMessage(String srcUserName, String groupName, String content) {
        this.content = content;
        this.groupName = groupName;
        this.srcUserName = srcUserName;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
