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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Slf4j
public class DocumentListenerThread implements Runnable {
    private File rootFile;

    @Override
    public void run() {
        try {
            Document newDocument = FileUtil.recursionPath(rootFile, rootFile.getAbsolutePath());
            List<DocumentDiff> documentDiffList = new ArrayList<>();

            // 获取配置
            FileSyncConfig config = SpringContextUtils.getBean(FileSyncConfig.class);
            Document oldDocument = config.getRootDocument();
            // ignore set
            Set<DocumentDiff> ignoreDocDiffSet = (Set<DocumentDiff>) SpringContextUtils.getBean("ignoreDocDiffSet");
            // 比较文件变动
            FileUtil.compareDocument(oldDocument, newDocument, documentDiffList, true, ignoreDocDiffSet);
            // 重置内存文档树
            config.setRootDocument(newDocument);
            if (CollectionUtils.isEmpty(documentDiffList)) {
                return;
            }
            log.info("文件变动: {}", GsonUtil.toJsonString(documentDiffList));
            // 上传文件变动
            TransferFileService transferFileService = SpringContextUtils.getBean(TransferFileService.class);
            documentDiffList.forEach(transferFileService::uploadDocumentDiffNetty);
        } catch (Exception e) {
            log.error("文件变动监听任务执行失败, err: {}", ExceptionUtils.getStackTrace(e));
        }
    }


}
