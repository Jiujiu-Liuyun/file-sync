package com.zhangyun.file.client.handler;

import com.zhangyun.file.client.service.ClientDocManageService;
import com.zhangyun.file.common.domain.NotifyDocDiff;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@ChannelHandler.Sharable
@Component
@Slf4j
public class NotifyDocDiffHandler extends SimpleChannelInboundHandler<NotifyDocDiff> {
    @Resource
    private BlockingQueue<DocumentDiff> documentDiffBlockingQueue;

    @Resource
    private Set<DocumentDiff> ignoreDocDiffSet;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NotifyDocDiff msg) throws Exception {
        log.info("接收到服务端的文档差异, notifyDocDiff: {}", msg);
        serverDocDiffProvider(msg.getDiff());
    }

    public void serverDocDiffProvider(DocumentDiff diff) {
        try {
            documentDiffBlockingQueue.put(diff);
            ignoreDocDiffSet.add(diff);
        } catch (InterruptedException e) {
            log.info("阻塞队列添加元素异常, err: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
