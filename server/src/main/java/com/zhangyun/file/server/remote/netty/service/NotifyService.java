package com.zhangyun.file.server.remote.netty.service;

import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.nettyMsg.NotifyDocDiff;
import com.zhangyun.file.common.uilt.GsonUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NotifyService {
    @Resource
    private SessionService sessionService;


    public void notifyDocDiff(DocDiff docDiff, String deviceId) {
        Map<String, Channel> session = sessionService.getOtherSession(deviceId);
        log.info("当前在线客户端, session: {}", session);
        if (MapUtils.isEmpty(session)) {
            return;
        }
        for (Map.Entry<String, Channel> entry : session.entrySet()) {
            log.info("通知客户端文档变更, diff: {}, entry: {}", docDiff, GsonUtil.toJsonString(entry));
            Channel channel = entry.getValue();
            channel.writeAndFlush(new NotifyDocDiff(docDiff));
        }
    }
}
