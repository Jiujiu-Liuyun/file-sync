package com.zhangyun.file.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
@Slf4j
public class GlobalExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("连接异常，断开连接，err: {}", ExceptionUtils.getStackTrace(cause));
        try {
            ctx.channel().close().sync();
        } catch (InterruptedException e) {
            log.error("断开连接失败，err: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
