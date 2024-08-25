package com.zhangyun.file.common.protocol;


import com.zhangyun.file.common.domain.nettyMsg.BaseMsg;
import com.zhangyun.file.common.uilt.GsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * description:
 *
 * @author: zhangyun
 * @date: 2022/7/30 02:05
 * @since: 1.0
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, BaseMsg> {

    private final int VERSION = 1;

    private final int SERIAL_ALGORITHM = 1;

    /**
     * 消息编码：从MessageDTO对象转为byte[]对象
     */
    @Override
    protected void encode(ChannelHandlerContext cxt, BaseMsg message, List<Object> list) throws Exception {
        // log trace
        log.info("encode msg: {}", GsonUtil.toJsonString(message));

        ByteBuf out = cxt.alloc().buffer();
        // magic --- 12bytes
        out.writeBytes(new byte[]{0x70, 0x6f, 0x72, 0x74, 0x61, 0x6c,
                0x6b, 0x6e, 0x69, 0x67, 0x68, 0x74});
        // version --- byte
        out.writeByte(VERSION);
        // 序列化算法 --- byte
        out.writeByte(SERIAL_ALGORITHM);
        // 指令类型 --- int
        out.writeInt(message.getMessageType());
        // 对齐填充 --- 18bytes + 14bytes = 32bytes
        byte[] alignBytes = new byte[14];
        Arrays.fill(alignBytes, (byte) 0xff);
        out.writeBytes(alignBytes);

        // msg serialization
        byte[] msgBytes = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(message);
            msgBytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("消息序列化失败, msg: {}, error: {}", GsonUtil.toJsonString(message), ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
        // msg length
        int length = msgBytes.length;
        // msg body
        out.writeInt(length);
        out.writeBytes(msgBytes);

        // send msg
        list.add(out);
    }

    /**
     * 消息解码
     */
    @Override
    protected void decode(ChannelHandlerContext cxt, ByteBuf in, List<Object> list) throws Exception {
        // magic
        ByteBuf magic = in.readBytes(12);
        // version
        byte version = in.readByte();
        if (version != this.VERSION) {
            log.warn("version don't match! version should be {}, but version is {}", this.VERSION, version);
        }
        // 序列化算法
        byte serialAlgorithm = in.readByte();
        if (serialAlgorithm != this.SERIAL_ALGORITHM) {
            log.warn("serial algorithm don't match! it should be {}, but it is {}", this.SERIAL_ALGORITHM, serialAlgorithm);
        }
        // 指令类型
        int messageType = in.readInt();
        Class<? extends BaseMsg> messageClass = BaseMsg.getMessageClass(messageType);
        // 对齐填充
        in.readBytes(14);

        // read msg length
        int length = in.readInt();
        // read msg body
        byte[] msgBytes = new byte[length];
        ByteBuf byteBuf = in.readBytes(length);
        byteBuf.readBytes(msgBytes);
        Object msgObj = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msgBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            msgObj = objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            log.error("消息序反列化失败, error: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        // receive msg
        list.add(msgObj);

        // log trace
        log.info("decode msg: {}", GsonUtil.toJsonString(msgObj));
    }
}
