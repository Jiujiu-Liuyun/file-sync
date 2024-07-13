package com.zhangyun.file.client.thread;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.net.TransferFileService;
import com.zhangyun.file.client.service.ClientNettyService;
import com.zhangyun.file.common.domain.doc.Document;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import com.zhangyun.file.common.uilt.SpringContextUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class DocumentListenerThread implements Runnable {
    private File rootFile;

    @Override
    public void run() {
        try {
            log.info("本地文件扫描");
            Document newDocument = FileUtil.recursionPath(rootFile, rootFile.getAbsolutePath());
            List<DocumentDiff> documentDiffList = new ArrayList<>();

            // 获取配置
            FileSyncConfig config = SpringContextUtils.getBean(FileSyncConfig.class);
            Document oldDocument = config.getRootDocument();
            // 比较文件变动
            FileUtil.compareDocument(oldDocument, newDocument, documentDiffList, true);
            log.info("文件变动: {}", GsonUtil.toJsonString(documentDiffList));
            // 上传文件变动
            TransferFileService transferFileService = SpringContextUtils.getBean(TransferFileService.class);
            documentDiffList.forEach(transferFileService::uploadDocumentDiffNetty);
            // 重置内存文档树
            config.setRootDocument(newDocument);
        } catch (Exception e) {
            log.error("文件变动监听任务执行失败, err: {}", ExceptionUtils.getStackTrace(e));
        }
    }


}
