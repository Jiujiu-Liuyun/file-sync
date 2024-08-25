package com.zhangyun.file.client.domain.doc;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.domain.doc.service.DocTransferService;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.doc.DocTree;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Data
public class DocManager implements InitializingBean {
    /**
     * 文档树
     */
    private DocTree docTree;
    /**
     * 本地差异队列
     */
    private BlockingQueue<DocDiff> localDocDiffQueue;
    /**
     * 远程差异队列
     */
    @Resource(name = "remoteDocDiffQueue")
    private BlockingQueue<DocDiff> remoteDocDiffQueue;

    @Resource
    private DocLockManager docLockManager;
    @Resource
    private FileSyncConfig fileSyncConfig;
    @Resource
    private ScheduledExecutorService scheduledExecutorService;
    @Resource
    private DocTransferService docTransferService;


    public void compareDocTree() {
        docLockManager.lockDocTree();
        try {
            // 1.遍历本地文档树
            File rootDir = new File(fileSyncConfig.getRootPath());
            DocTree docTreeNew = FileUtil.visitPath(rootDir, fileSyncConfig.getRootPath());
            // 2.比较文档树
            List<DocDiff> docDiffListNew = new ArrayList<>();
            FileUtil.compareDocTree(docTree, docTreeNew, docDiffListNew);
            log.info("文档树比较结果, docDiffListNew: {}", GsonUtil.toJsonString(docDiffListNew));
            // 3.合并文档树
            mergeDocDiffs(docDiffListNew);
            // 4.替换文档树
            docTree = docTreeNew;
        } catch (Exception e) {
            log.error("文档树比较失败, e: {}", ExceptionUtils.getStackTrace(e));
        } finally {
            docLockManager.unlockDocTree();
        }
    }

    // todo: 暂时简单合并
    private void mergeDocDiffs(List<DocDiff> docDiffListNew) {
        if (CollectionUtils.isEmpty(docDiffListNew)) {
            return;
        }
        docDiffListNew.forEach(docDiff -> {
            try {
                localDocDiffQueue.put(docDiff);
            } catch (InterruptedException e) {
                log.error("docDiffs添加失败, docDiff: {}", docDiff);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 初始化-文档资源树根节点
     */
    @Override
    public void afterPropertiesSet() {
        localDocDiffQueue = new ArrayBlockingQueue<>(1024);
        File rootDir = new File(fileSyncConfig.getRootPath());
        docTree = FileUtil.visitPath(rootDir, fileSyncConfig.getRootPath());
        log.info("文档树初始化成功, rootTree: {}", GsonUtil.toJsonString(docTree));
        // 定时比较文档差异
        scheduledExecutorService.scheduleWithFixedDelay(this::compareDocTree, fileSyncConfig.getInterval(), fileSyncConfig.getInterval(), TimeUnit.SECONDS);
        // 开启本地文档差异列表消费
        startConsumeLocalDocDiff();
        // 开启远程文档差异列表消费
        startConsumeRemoteDocDiff();
    }

    public void startConsumeLocalDocDiff() {
        new Thread(() -> {
            while (true) {
                try {
                    DocDiff docDiff = localDocDiffQueue.take();
                    log.info("处理本地文档差异, diff: {}", docDiff);
                    docTransferService.uploadDocDiff(docDiff);
                } catch (Exception e) {
                    log.error("消费本地文档差异失败, e: {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }).start();
    }

    public void startConsumeRemoteDocDiff() {
        new Thread(() -> {
            while (true) {
                try {
                    DocDiff diff = remoteDocDiffQueue.take();
                    log.info("处理远程文档差异, diff: {}", diff);
                    docTransferService.downloadDocDiff(diff);
                    // 更改文档树
                    DocTree.updateTreeNode(docTree, Paths.get(diff.getDocIdentity().getRelativePath()), Paths.get(fileSyncConfig.getRootPath()), diff.getDiffTypeEnum());
                } catch (Exception e) {
                    log.error("消费文档差异数据失败, {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }).start();
    }
}
