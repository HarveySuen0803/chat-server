package com.harvey.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserLoginResponseMessage extends AbstractResponseMessage {
    public UserLoginResponseMessage() {
        super();
    }
    
    public UserLoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    
    @Override
    public int getMessageType() {
        return UserLoginResponseMessage;
    }
}
