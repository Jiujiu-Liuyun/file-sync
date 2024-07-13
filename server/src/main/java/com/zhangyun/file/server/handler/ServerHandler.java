package com.zhangyun.file.server.handler;

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
    private UploadDocDiffReqHandler uploadDocDiffReqHandler;

    /**
     * 初始化通道以及配置对应管道的处理器
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new FrameDecoder());
        pipeline.addLast(messageCodecSharable);
        pipeline.addLast(uploadDocDiffReqHandler);
    }
}

