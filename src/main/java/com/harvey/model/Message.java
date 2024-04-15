package com.harvey.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int UserLoginRequestMessage = 0;
    public static final int UserLoginResponseMessage = 1;
    public static final int UserChatRequestMessage = 2;
    public static final int UserChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupUserJoinRequestMessage = 6;
    public static final int GroupUserJoinResponseMessage = 7;
    public static final int GroupUserQuitRequestMessage = 8;
    public static final int GroupUserQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupUserListRequestMessage = 12;
    public static final int GroupUserListResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    
    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(UserLoginRequestMessage, UserLoginRequestMessage.class);
        messageClasses.put(UserLoginResponseMessage, UserLoginResponseMessage.class);
        messageClasses.put(UserChatRequestMessage, com.harvey.model.UserChatRequestMessage.class);
        messageClasses.put(UserChatResponseMessage, com.harvey.model.UserChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        messageClasses.put(GroupUserJoinRequestMessage, com.harvey.model.GroupUserJoinRequestMessage.class);
        messageClasses.put(GroupUserJoinResponseMessage, com.harvey.model.GroupUserJoinResponseMessage.class);
        messageClasses.put(GroupUserQuitRequestMessage, com.harvey.model.GroupUserQuitRequestMessage.class);
        messageClasses.put(GroupUserQuitResponseMessage, com.harvey.model.GroupUserQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, GroupChatResponseMessage.class);
        messageClasses.put(GroupUserListRequestMessage, GroupUserListRequestMessage.class);
        messageClasses.put(GroupUserListResponseMessage, GroupUserListResponseMessage.class);
    }
}
