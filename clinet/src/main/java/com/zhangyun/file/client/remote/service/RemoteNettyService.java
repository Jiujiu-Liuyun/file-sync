package com.zhangyun.file.client.remote.service;

import com.zhangyun.file.common.domain.nettyMsg.BaseMsg;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RemoteNettyService {
    @Resource
    private ChannelFuture channel;

    public void sendMessage(BaseMsg msg) {
        if (channel == null) {
            log.error("和netty服务器连接失败，发送消息失败");
        }
        channel.channel().writeAndFlush(msg);
    }

}
