package com.harvey.client;

import com.harvey.client.handler.ChatHandler;
import com.harvey.client.handler.HeartBeatSendHandler;
import com.harvey.protocol.MessageCodecSharable;
import com.harvey.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatClient {
    private static LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
    
    private static MessageCodecSharable messageCodec = new MessageCodecSharable();
    
    public static ChatHandler chatHandler = new ChatHandler();
    
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(messageCodec);
                    ch.pipeline().addLast(new IdleStateHandler(0, 20, 0));
                    ch.pipeline().addLast(new HeartBeatSendHandler());
                    ch.pipeline().addLast(chatHandler);
                }
            });
            Channel channel = bootstrap.connect("localhost", 10100).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
