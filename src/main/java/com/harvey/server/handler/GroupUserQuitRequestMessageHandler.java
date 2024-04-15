package com.harvey.server.handler;

import com.harvey.model.GroupUserQuitRequestMessage;
import com.harvey.model.GroupUserQuitResponseMessage;
import com.harvey.service.GroupService;
import com.harvey.service.GroupServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Harvey Suen
 */
@Sharable
public class GroupUserQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupUserQuitRequestMessage> {
    
    private GroupService groupService = GroupServiceFactory.getGroupService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupUserQuitRequestMessage reqMsg) throws Exception {
        String groupName = reqMsg.getGroupName();
        String srcUserName = reqMsg.getSrcUserName();
        
        boolean isRemoveSuccess = groupService.delUser(groupName, srcUserName);
        if (isRemoveSuccess) {
            ctx.channel().writeAndFlush(new GroupUserQuitResponseMessage(true, "Successfully quit the group"));
        } else {
            ctx.channel().writeAndFlush(new GroupUserQuitResponseMessage(false, "Failed to quit the group"));
        }
    }
    
}
