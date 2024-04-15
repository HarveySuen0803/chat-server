package com.harvey.server.handler;

import com.harvey.session.Session;
import com.harvey.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Harvey Suen
 */
@Slf4j
@Sharable
public class UserQuitHandler extends ChannelInboundHandlerAdapter {
    private Session session = SessionFactory.getSession();
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        session.unbind(ctx.channel());
        log.debug("Normal quiting, successfully closed the channel, channel: {}", ctx.channel());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        session.unbind(ctx.channel());
        log.debug("Abnormal quiting, successfully closed the channel, channel: {}, exception info: {}", ctx.channel(), cause.getMessage());
    }
}
