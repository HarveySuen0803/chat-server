package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserLoginRequestMessage extends Message {
    private String userName;
    private String password;

    public UserLoginRequestMessage() {
    }

    public UserLoginRequestMessage(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return UserLoginRequestMessage;
    }
}
