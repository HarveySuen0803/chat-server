package com.harvey.server.handler;

import com.harvey.model.GroupCreateRequestMessage;
import com.harvey.model.GroupCreateResponseMessage;
import com.harvey.service.GroupService;
import com.harvey.service.GroupServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Harvey Suen
 */
@Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    private GroupService groupService = GroupServiceFactory.getGroupService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage reqMsg) throws Exception {
        String groupName = reqMsg.getGroupName();
        Set<String> userNameSet = reqMsg.getUserNameSet();
        boolean isCreateSuccess = groupService.addGroup(groupName, userNameSet);
        if (isCreateSuccess) {
            ctx.channel().writeAndFlush(new GroupCreateResponseMessage(true, "Successfully created the group"));
            
            List<Channel> memberChannelList = groupService.getUserChannelList(groupName);
            for (Channel memberChannel : memberChannelList) {
                memberChannel.writeAndFlush(new GroupCreateResponseMessage(true, "You have been added to the group chat"));
            }
        } else {
            ctx.channel().writeAndFlush(new GroupCreateResponseMessage(false, "Failed to create the group"));
        }
    }
}
