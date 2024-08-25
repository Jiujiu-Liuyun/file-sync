package com.zhangyun.file.server.remote.netty.handler;

import com.zhangyun.file.server.remote.netty.service.SessionService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SessionHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private SessionService sessionService;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接断开, channel: {}", ctx.channel());
        sessionService.logout(ctx.channel());
        super.channelInactive(ctx);
    }
}
