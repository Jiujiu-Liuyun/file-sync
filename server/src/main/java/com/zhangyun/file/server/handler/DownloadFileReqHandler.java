package com.zhangyun.file.server.handler;

import com.zhangyun.file.common.domain.req.DownloadFileReq;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.DownloadFileResp;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.server.config.FileSyncConfig;
import com.zhangyun.file.server.config.NettyConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@Component
@ChannelHandler.Sharable
public class DownloadFileReqHandler extends SimpleChannelInboundHandler<DownloadFileReq> {
    @Resource
    private FileSyncConfig config;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DownloadFileReq msg) throws Exception {
        String path = FileUtil.getAbsolutePath(msg.getRelativePath(), config.getPath());
        String content = FileUtil.readFile(new File(path));
        ctx.writeAndFlush(new DownloadFileResp(msg.getDiffTypeEnum(), msg.getName(), msg.getRelativePath(), msg.getTypeEnum(), content));
    }
}
