package com.zhangyun.file.client.remote.service;

import com.zhangyun.file.client.config.NettyConfig;
import com.zhangyun.file.common.domain.nettyMsg.BaseMsg;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RemoteNettyService {
    @Resource
    private NettyConfig nettyConfig;

    public void sendMessage(BaseMsg msg) {
        ChannelFuture channel = nettyConfig.getChannelFuture();
        if (channel == null) {
            log.error("和netty服务器连接失败，发送消息失败");
            return;
        }
        channel.channel().writeAndFlush(msg);
    }

}
