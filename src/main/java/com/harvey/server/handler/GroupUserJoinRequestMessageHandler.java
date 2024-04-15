package com.harvey.server.handler;

import com.harvey.model.GroupUserJoinRequestMessage;
import com.harvey.model.GroupUserJoinResponseMessage;
import com.harvey.service.GroupService;
import com.harvey.service.GroupServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Harvey Suen
 */
@Sharable
public class GroupUserJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupUserJoinRequestMessage> {
    private GroupService groupService = GroupServiceFactory.getGroupService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupUserJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String srcUserName = msg.getSrcUserName();
        boolean isJoinSuccess = groupService.addUser(groupName, srcUserName);
        if (isJoinSuccess) {
            ctx.channel().writeAndFlush(new GroupUserJoinResponseMessage(true, "Successfully joined the group, groupName"));
        } else {
            ctx.channel().writeAndFlush(new GroupUserJoinResponseMessage(false, "Failed to join the group, groupName"));
        }
    }
}
