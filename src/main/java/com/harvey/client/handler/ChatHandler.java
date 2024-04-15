package com.harvey.client.handler;

import com.harvey.model.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.netty.channel.ChannelHandler.Sharable;

@Slf4j
@Sharable
public class ChatHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch latchForLogin = new CountDownLatch(1);
    
    private AtomicBoolean isUserLoginSuccess = new AtomicBoolean(false);
    
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendRequest(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Receive msg: {}", msg);
        
        if (msg instanceof UserLoginResponseMessage) {
            UserLoginResponseMessage repMsg = (UserLoginResponseMessage) msg;
            
            if (repMsg.isSuccess()) {
                isUserLoginSuccess.set(true);
            }
            
            latchForLogin.countDown();
        }
    }
    
    private void sendRequest(ChannelHandlerContext ctx) {
        // 单独开启一个线程用于处理用户登录后的请求, 防止占用 EventLoopGroup 中的工作线程
        executorService.submit(() -> {
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            
            System.out.println("==================================");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Input username: ");
            String userName = scanner.nextLine();
            System.out.print("Input password: ");
            String password = scanner.nextLine();
            UserLoginRequestMessage msg = new UserLoginRequestMessage(userName, password);
            ctx.writeAndFlush(msg);
            
            try {
                latchForLogin.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            
            if (!isUserLoginSuccess.get()) {
                ctx.channel().close();
            }
            
            sendCmdRequest(userName, ctx);
        });
    }
    
    private void sendCmdRequest(String userName, ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            
            System.out.println("==================================");
            System.out.println("user_chat [userName] [content]");
            System.out.println("group_chat [groupName] [content]");
            System.out.println("group_create [groupName] [userName1,userName2,userName3...]");
            System.out.println("group_user_list [groupName]");
            System.out.println("group_user_join [groupName]");
            System.out.println("group_user_quit [groupName]");
            System.out.println("user_quit");
            System.out.println("==================================");
            
            System.out.print("Input cmd: ");
            String cmd = scanner.nextLine();
            log.debug("Receive cmd: {}", cmd);
            
            String[] cmdCells = cmd.split(" ");
            
            switch (cmdCells[0]) {
                case "user_chat":
                    ctx.writeAndFlush(new UserChatRequestMessage(userName, cmdCells[1], cmdCells[2]));
                    break;
                case "group_chat":
                    ctx.writeAndFlush(new GroupChatRequestMessage(userName, cmdCells[1], cmdCells[2]));
                    break;
                case "group_create":
                    Set<String> userNameSet = Arrays.stream(cmdCells[2].split(",")).collect(Collectors.toSet());
                    ctx.writeAndFlush(new GroupCreateRequestMessage(cmdCells[1], userNameSet));
                    break;
                case "group_user_list":
                    ctx.writeAndFlush(new GroupUserListRequestMessage(cmdCells[1]));
                    break;
                case "group_user_join":
                    ctx.writeAndFlush(new GroupUserJoinRequestMessage(userName, cmdCells[1]));
                    break;
                case "group_user_quit":
                    ctx.writeAndFlush(new GroupUserQuitRequestMessage(userName, cmdCells[1]));
                    break;
                case "user_quit":
                    ctx.channel().close();
                    return;
            }
        }
    }
}