package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.thread.DocumentListenerThread;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.domain.req.DownloadFileReq;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
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
//        File file = new File(clientConfig.getRootPath());
//        clientConfig.setRootDocument(FileUtil.recursionPath(file, clientConfig.getRootPath()));
//        log.info("初始化文档根目录, {}", GsonUtil.toJsonString(clientConfig.getRootDocument()));
//        scheduledExecutorService.scheduleWithFixedDelay(new DocumentListenerThread(file), clientConfig.getInterval(), clientConfig.getInterval(), TimeUnit.SECONDS);
    }

    public void handleDocDiff(DocumentDiff diff) {
        String path = diff.getDocAbsolutePath(clientConfig.getRootPath());
        File file = new File(path);
        if (diff.getTypeEnum() == DocumentTypeEnum.FILE && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            // 请求下载文件
            clientNettyService.sendMessage(new DownloadFileReq(diff.getDiffTypeEnum(), diff.getName(), diff.getRelativePath(), diff.getTypeEnum()));
        } else if (diff.getTypeEnum() == DocumentTypeEnum.FILE) {
            FileUtil.deleteFile(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.CREATE) {
            FileUtil.createDir(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            FileUtil.deleteDir(file);
        }
    }
}
