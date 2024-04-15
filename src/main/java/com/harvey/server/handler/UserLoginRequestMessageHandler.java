package com.harvey.server.handler;

import com.harvey.model.UserLoginRequestMessage;
import com.harvey.model.UserLoginResponseMessage;
import com.harvey.service.UserService;
import com.harvey.service.UserServiceFactory;
import com.harvey.session.Session;
import com.harvey.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class UserLoginRequestMessageHandler extends SimpleChannelInboundHandler<UserLoginRequestMessage> {
    private Session session = SessionFactory.getSession();
    
    private UserService userService = UserServiceFactory.getUserService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserLoginRequestMessage reqMsg) throws Exception {
        String userName = reqMsg.getUserName();
        String password = reqMsg.getPassword();
        boolean isLoginSuccess = userService.login(userName, password);
        UserLoginResponseMessage repMsg;
        if (isLoginSuccess) {
            session.bind(ctx.channel(), userName);
            repMsg = new UserLoginResponseMessage(true, "Successfully login the chat server");
        } else {
            repMsg = new UserLoginResponseMessage(false, "Failed to login the chat server");
        }
        ctx.writeAndFlush(repMsg);
    }
}
