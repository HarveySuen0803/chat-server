package com.harvey.client.handler;

import com.harvey.model.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Harvey Suen
 */
@Slf4j
public class HeartBeatSendHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        
        if (event.state() == IdleState.WRITER_IDLE) {
            log.debug("No write for 20 seconds, sending heartbeat");
            ctx.writeAndFlush(new PingMessage());
        }
    }
}
