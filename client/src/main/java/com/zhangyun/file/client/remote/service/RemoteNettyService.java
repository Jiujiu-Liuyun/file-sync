package com.zhangyun.file.client.remote.service;

import com.zhangyun.file.client.config.NettyConfig;
import com.zhangyun.file.client.remote.handler.ClientHandler;
import com.zhangyun.file.common.domain.nettyMsg.BaseMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.awt.image.OffScreenImage;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RemoteNettyService implements InitializingBean {
    @Resource
    private NettyConfig nettyConfig;
    @Resource
    private ClientHandler clientHandler;
    @Resource
    private ScheduledExecutorService scheduledExecutorService;


    public void sendMessage(BaseMsg msg) {
        ChannelFuture channelFuture = nettyConfig.getChannelFuture();
        if (channelFuture == null) {
            log.error("和netty服务器连接失败，发送消息失败");
            return;
        }
        channelFuture.channel().writeAndFlush(msg);
    }

    /**
     * netty客户端 channel
     */
    public void connect() {
        try {
            ChannelFuture channelFuture = nettyConfig.getBootstrap().connect(nettyConfig.getHost(), nettyConfig.getPort()).sync();
            log.info("netty客户端启动成功, channelFuture: {}", channelFuture);
            nettyConfig.setChannelFuture(channelFuture);
        } catch (Exception e) {
            log.error("netty客户端启动失败, err: {}", ExceptionUtils.getStackTrace(e));
            scheduledExecutorService.schedule(this::connect, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 新建一组线程池
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(nettyConfig.getWorker());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)   // 指定线程组
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class) // 指定通道
                .handler(clientHandler); // 指定处理器
        nettyConfig.setBootstrap(bootstrap);
        // 连接netty服务端
        scheduledExecutorService.schedule(this::connect, 0, TimeUnit.SECONDS);
    }
}
