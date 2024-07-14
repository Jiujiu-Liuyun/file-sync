package com.zhangyun.file.server.handler;

import com.zhangyun.file.server.service.SessionService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SessionManageHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private SessionService sessionService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sessionService.register(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionService.offline(ctx.channel());
        super.channelInactive(ctx);
    }
}
