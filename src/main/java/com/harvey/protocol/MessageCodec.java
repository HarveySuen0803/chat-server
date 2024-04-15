package com.harvey.protocol;

import com.harvey.config.CommonConfig;
import com.harvey.model.UserLoginRequestMessage;
import com.harvey.model.Message;
import com.harvey.serializer.SerializerFactory;
import com.harvey.serializer.SerializerJavaImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author Harvey Suen
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 魔术 (4B)
        out.writeBytes(new byte[]{'C', 'A', 'F', 'E'});
        
        // 版本 (1B)
        out.writeByte(1);
        
        // 序列化方式 (1: JDK, 2: JSON, 3: Hessian) (1B)
        out.writeByte(CommonConfig.getSerializerType());
        
        // 消息类型 (1B)
        out.writeByte(msg.getMessageType());
        
        // 消息序列号, 用于双工通信 (4B)
        out.writeInt(msg.getSequenceId());
        
        // 对齐 (1B), 保证组成部分 4 + 1 + 1 + 1 + 1 + 4 + 4 B = 16B (除了消息内容) 为 2 的整数倍, 专业一点 ^_^
        out.writeByte(0xff);
        
        // 通过 JDK 进行序列化
        byte[] bytes = SerializerFactory.getSerializer(CommonConfig.getSerializerType()).serialize(msg);
        
        // 消息内容长度 (4B)
        out.writeInt(bytes.length);

        // 消息内容
        out.writeBytes(bytes);
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> msgList) throws Exception {
        // 魔术
        int magicNum = in.readInt();
        
        // 版本
        byte version = in.readByte();
        
        // 序列化器类型
        byte serializerType = in.readByte();
        
        // 消息类型
        byte messageType = in.readByte();
        
        // 消息序列号
        int sequenceId = in.readInt();
        
        // 对齐
        in.readByte();
        
        // 消息内容长度
        int len = in.readInt();
        
        // 通过 JDK 进行反序列化
        byte[] bytes = new byte[len];
        in.readBytes(bytes, 0, len);
        
        Message msg = SerializerFactory.getSerializer(serializerType).deserialize(bytes, Message.getMessageClass(messageType));
        
        log.debug("Decoded msg info: {}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, len);
        log.debug("Decoded msg: {}", msg);
        
        msgList.add(msg);
    }

    public static void main(String[] args) throws Exception {
        LoggingHandler loggingHandler = new LoggingHandler();
        LengthFieldBasedFrameDecoder frameDecoder = new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0);
        EmbeddedChannel channel = new EmbeddedChannel(
            frameDecoder,
            loggingHandler,
            new MessageCodec()
        );
        
        UserLoginRequestMessage srcMsg = new UserLoginRequestMessage("Harvey Suen", "111");
        channel.writeOutbound(srcMsg);
        ByteBuf tarMsg = channel.readOutbound();
        
        // 将一个 ByteBuf 通过 slice() 拆分成两个 ByteBuf 来模拟半包问题
        ByteBuf tarMsgSlice1 = tarMsg.slice(0, 100);
        ByteBuf tarMsgSlice2 = tarMsg.slice(100, tarMsg.readableBytes() - 100);
        
        // slice() 拆分后的 ByteBuf() 实际是 Zero Copy 的体现, 底层还是公用的原来的物理内存,
        // channel.writeInBound(tarMsgSlice1) 执行后, 会去进行 release(), 减少一次 tarMsgSlice1 的引用计数,
        // 实际上间的就是 tarMsg 的引用计数, 导致 tarMsgSlice1 和 tarMsgSlice2 都失效了,
        // 所以这里需要执行 tarMsgSlice().retain() 防止被错误释放
        tarMsgSlice1.retain();
        
        channel.writeInbound(tarMsgSlice1);
        channel.writeInbound(tarMsgSlice2);
    }
    
    private static void byteBufLog(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
            .append("read index: ").append(buffer.readerIndex())
            .append(",  write index: ").append(buffer.writerIndex())
            .append(",  capacity: ").append(buffer.capacity())
            .append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
