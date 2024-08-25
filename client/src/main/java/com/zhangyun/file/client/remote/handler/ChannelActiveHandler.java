package com.zhangyun.file.client.remote.handler;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.config.NettyConfig;
import com.zhangyun.file.common.domain.nettyMsg.DeviceLogin;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private FileSyncConfig fileSyncConfig;
    @Resource
    private NettyConfig nettyConfig;
    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DeviceLogin deviceLogin = new DeviceLogin(fileSyncConfig.getDeviceId());
        log.info("客户端连接成功，发送设备登录信息, deviceLogin: {}", deviceLogin);
        ctx.writeAndFlush(deviceLogin);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("netty连接断开");
        scheduledExecutorService.schedule(this::reconnect, 0, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    /**
     * netty客户端 channel
     */
    public void reconnect() {
        try {
            ChannelFuture channelFuture = nettyConfig.getBootstrap().connect(nettyConfig.getHost(), nettyConfig.getPort()).sync();
            log.info("netty重连成功, channelFuture: {}", channelFuture);
            nettyConfig.setChannelFuture(channelFuture);
        } catch (Exception e) {
            log.error("netty重连失败, err: {}", ExceptionUtils.getStackTrace(e));
            scheduledExecutorService.schedule(this::reconnect, 5, TimeUnit.SECONDS);
        }
    }
}
