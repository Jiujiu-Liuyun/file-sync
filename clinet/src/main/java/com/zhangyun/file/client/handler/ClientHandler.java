package com.zhangyun.file.client.handler;

import com.zhangyun.file.common.protocol.FrameDecoder;
import com.zhangyun.file.common.protocol.MessageCodecSharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ClientHandler extends ChannelInitializer<SocketChannel> {
    @Resource
    private MessageCodecSharable messageCodecSharable;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new FrameDecoder());
        pipeline.addLast(messageCodecSharable);
    }
}