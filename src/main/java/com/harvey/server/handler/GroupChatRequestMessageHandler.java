package com.harvey.server.handler;

import com.harvey.model.GroupChatRequestMessage;
import com.harvey.model.GroupChatResponseMessage;
import com.harvey.service.GroupService;
import com.harvey.service.GroupServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author Harvey Suen
 */
@Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    private GroupService groupService = GroupServiceFactory.getGroupService();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage reqMsg) throws Exception {
        String groupName = reqMsg.getGroupName();
        List<Channel> tarUserChannelList = groupService.getUserChannelList(groupName);
        for (Channel tarUserChannel : tarUserChannelList) {
            tarUserChannel.writeAndFlush(new GroupChatResponseMessage(reqMsg.getSrcUserName(), reqMsg.getContent()));
        }
    }
}
