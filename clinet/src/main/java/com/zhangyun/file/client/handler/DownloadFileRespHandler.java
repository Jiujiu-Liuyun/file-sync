package com.zhangyun.file.client.handler;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.common.domain.NotifyDocDiff;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import com.zhangyun.file.common.domain.resp.DownloadFileResp;
import com.zhangyun.file.common.uilt.FileUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@ChannelHandler.Sharable
@Component
@Slf4j
public class DownloadFileRespHandler extends SimpleChannelInboundHandler<DownloadFileResp> {
    @Resource
    private FileSyncConfig config;
    @Resource
    private Set<DocumentDiff> ignoreDocDiffSet;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DownloadFileResp msg) throws Exception {
        log.info("收到下载文件响应: {}", msg);
        String path = FileUtil.getAbsolutePath(msg.getRelativePath(), config.getPath());
        FileUtil.writeFile(msg.getContent(), new File(path));
        ignoreDocDiffSet.remove(new DocumentDiff(msg.getDiffTypeEnum(), msg.getName(), msg.getRelativePath(), msg.getTypeEnum()));
    }
}
