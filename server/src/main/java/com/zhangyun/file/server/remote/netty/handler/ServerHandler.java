package com.zhangyun.file.server.remote.netty.handler;

import com.zhangyun.file.common.protocol.FrameDecoder;
import com.zhangyun.file.common.protocol.MessageCodecSharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ServerHandler extends ChannelInitializer<SocketChannel> {

    @Resource
    private MessageCodecSharable messageCodecSharable;
    @Resource
    private DeviceLoginHandler deviceLoginHandler;
    @Resource
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * 初始化通道以及配置对应管道的处理器
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new FrameDecoder());
        pipeline.addLast(messageCodecSharable);
        pipeline.addLast(deviceLoginHandler);


        pipeline.addLast(globalExceptionHandler);
    }
}

