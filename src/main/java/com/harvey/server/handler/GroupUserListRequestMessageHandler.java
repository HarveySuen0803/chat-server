package com.harvey.server.handler;

import com.harvey.model.GroupUserListRequestMessage;
import com.harvey.model.GroupUserListResponseMessage;
import com.harvey.service.GroupService;
import com.harvey.service.GroupServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

import static io.netty.channel.ChannelHandler.*;

/**
 * @author Harvey Suen
 */
@Sharable
public class GroupUserListRequestMessageHandler extends SimpleChannelInboundHandler<GroupUserListRequestMessage> {
    private GroupService groupService = GroupServiceFactory.getGroupService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupUserListRequestMessage reqMsg) throws Exception {
        String groupName = reqMsg.getGroupName();
        Set<String> userNameList = groupService.getUserNameList(groupName);
        ctx.channel().writeAndFlush(new GroupUserListResponseMessage(userNameList));
    }
}
