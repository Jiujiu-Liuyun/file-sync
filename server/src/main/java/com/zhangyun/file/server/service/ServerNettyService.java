package com.zhangyun.file.server.service;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.server.config.NettyConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ServerNettyService implements InitializingBean {
    @Resource
    private ServerBootstrap serverBootstrap;
    @Resource
    private NettyConfig nettyConfig;

    @Getter
    private ChannelFuture channel;

    @Override
    public void afterPropertiesSet() {
        try {
            channel = serverBootstrap.bind(nettyConfig.getPort()).sync();
            log.info("netty服务端启动成功");
        } catch (InterruptedException e) {
            log.info("netty服务端启动失败, err: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void sendMessage(BaseMsg msg) {
        channel.channel().writeAndFlush(msg);
    }
}
