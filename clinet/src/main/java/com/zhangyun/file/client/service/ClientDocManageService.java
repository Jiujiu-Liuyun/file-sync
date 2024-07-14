package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.thread.DocumentListenerThread;
import com.zhangyun.file.common.domain.doc.Document;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import com.zhangyun.file.common.domain.req.DownloadFileReq;
import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class ClientDocManageService implements InitializingBean {
    @Resource
    private FileSyncConfig clientConfig;
    @Resource
    private ScheduledExecutorService scheduledExecutorService;
    @Resource
    private ClientNettyService clientNettyService;

    @Override
    public void afterPropertiesSet() {
        File file = new File(clientConfig.getPath());
        clientConfig.setRootDocument(FileUtil.recursionPath(file, clientConfig.getPath()));
        log.info("初始化文档根目录, {}", GsonUtil.toJsonString(clientConfig.getRootDocument()));
        scheduledExecutorService.scheduleWithFixedDelay(new DocumentListenerThread(file), clientConfig.getInterval(), clientConfig.getInterval(), TimeUnit.SECONDS);
    }

    public void handleDocDiff(DocumentDiff diff) {
        String path = diff.getDocAbsolutePath(clientConfig.getPath());
        File file = new File(path);
        if (diff.getTypeEnum() == DocumentTypeEnum.FILE && diff.getDiffTypeEnum() != DocumentDiffTypeEnum.DELETE) {
            // 请求下载文件
            clientNettyService.sendMessage(new DownloadFileReq(diff.getDiffTypeEnum(), diff.getName(), diff.getRelativePath(), diff.getTypeEnum()));
        } else if (diff.getTypeEnum() == DocumentTypeEnum.FILE) {
            FileUtil.deleteFile(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocumentDiffTypeEnum.CREATE) {
            FileUtil.createDir(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocumentDiffTypeEnum.DELETE) {
            FileUtil.deleteDir(file);
        }
    }
}
