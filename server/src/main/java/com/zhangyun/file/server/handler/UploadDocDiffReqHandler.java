package com.zhangyun.file.server.handler;

import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.uilt.GsonUtil;
import com.zhangyun.file.server.service.ServerDocManageService;
import com.zhangyun.file.server.service.SessionService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class UploadDocDiffReqHandler extends SimpleChannelInboundHandler<UploadDocDiffReq> {

    @Resource
    private ServerDocManageService serverDocManageService;

    @Resource
    private SessionService sessionService;

    @Override
    @Timer
    protected void channelRead0(ChannelHandlerContext ctx, UploadDocDiffReq msg) throws Exception {
        log.info("收到上传文件请求消息, msg: {}, channel: {}, b: {}", GsonUtil.toJsonString(msg), ctx.channel(), sessionService.isOnline(ctx.channel()));
        serverDocManageService.handleDocumentDiff(msg);
        ctx.fireChannelRead(msg);
    }

}
