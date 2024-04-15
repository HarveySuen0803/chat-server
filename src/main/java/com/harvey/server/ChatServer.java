package com.harvey.server;

import com.harvey.protocol.MessageCodecSharable;
import com.harvey.protocol.ProtocolFrameDecoder;
import com.harvey.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
    
    public static MessageCodecSharable messageCodec = new MessageCodecSharable();
    
    private static UserLoginRequestMessageHandler userLoginRequestMessageHandler = new UserLoginRequestMessageHandler();
    
    private static ChatRequestMessageHandler userChatRequestMessageHandler = new ChatRequestMessageHandler();
    
    private static GroupCreateRequestMessageHandler groupCreateRequestMessageHandler = new GroupCreateRequestMessageHandler();
    
    private static GroupChatRequestMessageHandler groupChatRequestMessageHandler = new GroupChatRequestMessageHandler();
    
    private static GroupUserJoinRequestMessageHandler groupUserJoinRequestMessageHandler = new GroupUserJoinRequestMessageHandler();
    
    private static GroupUserQuitRequestMessageHandler groupUserQuitRequestMessageHandler = new GroupUserQuitRequestMessageHandler();
    
    private static GroupUserListRequestMessageHandler groupUserListRequestMessageHandler = new GroupUserListRequestMessageHandler();
    
    private static UserQuitHandler userQuitHandler = new UserQuitHandler();
    
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    
                    ch.pipeline().addLast(loggingHandler);
                    
                    ch.pipeline().addLast(messageCodec);
                    
                    ch.pipeline().addLast(new IdleStateHandler(30, 0, 0));
                    
                    ch.pipeline().addLast(new HeartBeatCheckHandler());
                    
                    ch.pipeline().addLast(userLoginRequestMessageHandler);
                    ch.pipeline().addLast(userChatRequestMessageHandler);
                    ch.pipeline().addLast(groupCreateRequestMessageHandler);
                    ch.pipeline().addLast(groupChatRequestMessageHandler);
                    ch.pipeline().addLast(groupUserJoinRequestMessageHandler);
                    ch.pipeline().addLast(groupUserQuitRequestMessageHandler);
                    ch.pipeline().addLast(groupUserListRequestMessageHandler);
                    
                    ch.pipeline().addLast(userQuitHandler);
                }
            });
            Channel channel = serverBootstrap.bind(10100).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
