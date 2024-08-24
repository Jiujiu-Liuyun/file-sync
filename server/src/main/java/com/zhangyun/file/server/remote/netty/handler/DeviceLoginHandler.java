package com.zhangyun.file.server.remote.netty.handler;

import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.nettyMsg.DeviceLogin;
import com.zhangyun.file.common.uilt.GsonUtil;
import com.zhangyun.file.server.service.ServerDocManageService;
import com.zhangyun.file.server.remote.netty.service.SessionService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class DeviceLoginHandler extends SimpleChannelInboundHandler<DeviceLogin> {

    @Resource
    private ServerDocManageService serverDocManageService;

    @Resource
    private SessionService sessionService;

    @Override
    @Timer
    protected void channelRead0(ChannelHandlerContext ctx, DeviceLogin msg) throws Exception {
        log.info("收到设备登录消息, msg: {}, channel: {}", GsonUtil.toJsonString(msg), ctx.channel());
        sessionService.login(msg.getDeviceId(), ctx.channel());
        super.channelActive(ctx);
    }

}
