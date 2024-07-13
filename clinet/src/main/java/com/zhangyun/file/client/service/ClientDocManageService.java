package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.thread.DocumentListenerThread;
import com.zhangyun.file.common.domain.doc.Document;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ClientDocManageService implements InitializingBean {
    @Resource
    private FileSyncConfig clientConfig;
    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void afterPropertiesSet() {
        File file = new File(clientConfig.getPath());
        clientConfig.setRootDocument(FileUtil.recursionPath(file, clientConfig.getPath()));
        log.info("初始化文档根目录, {}", GsonUtil.toJsonString(clientConfig.getRootDocument()));
        scheduledExecutorService.scheduleWithFixedDelay(new DocumentListenerThread(file), clientConfig.getInterval(), clientConfig.getInterval(), TimeUnit.SECONDS);
    }
}
