package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.NettyConfig;
import com.zhangyun.file.common.domain.BaseMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ClientNettyService implements InitializingBean {
    @Resource
    private Bootstrap bootstrap;
    @Resource
    private NettyConfig nettyConfig;

    @Getter
    private ChannelFuture channel;

    @Override
    public void afterPropertiesSet() {
        try {
            channel = bootstrap.connect(nettyConfig.getHost(), nettyConfig.getPort()).sync();
            log.info("netty客户端启动成功");
        } catch (Exception e) {
            log.error("netty客户端启动失败, err: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void sendMessage(BaseMsg msg) {
        if (channel == null) {
            log.error("和netty服务器连接失败，发送消息失败");
        }
        channel.channel().writeAndFlush(msg);
    }
}
