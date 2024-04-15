package com.harvey.server.handler;

import com.harvey.model.UserChatRequestMessage;
import com.harvey.model.UserChatResponseMessage;
import com.harvey.session.Session;
import com.harvey.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import static io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<UserChatRequestMessage> {
    private Session session = SessionFactory.getSession();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserChatRequestMessage reqMsg) throws Exception {
        String content = reqMsg.getContent();
        String srcUserName = reqMsg.getSrcUserName();
        String tarUserName = reqMsg.getTarUserName();
        Channel tarChannel = session.getChannel(tarUserName);
        Channel srcChannel = ctx.channel();
        
        // 目标用户在线
        if (tarChannel != null) {
            tarChannel.writeAndFlush(new UserChatResponseMessage(srcUserName, content));
        }
        // 目标用户离线
        else {
            srcChannel.writeAndFlush(new UserChatResponseMessage(false, "The target user does not exist"));
        }
    }
}