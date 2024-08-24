package com.zhangyun.file.client.remote.handler;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.common.domain.nettyMsg.DeviceLogin;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private FileSyncConfig fileSyncConfig;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DeviceLogin deviceLogin = new DeviceLogin(fileSyncConfig.getDeviceId());
        log.info("客户端连接成功，发送设备登录信息, deviceLogin: {}", deviceLogin);
        ctx.writeAndFlush(deviceLogin);
        super.channelActive(ctx);
    }
}
