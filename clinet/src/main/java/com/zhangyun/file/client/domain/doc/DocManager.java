package com.zhangyun.file.client.domain.doc;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.doc.DocTree;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
@Data
public class DocManager implements InitializingBean {
    /**
     * 文档资源树
     */
    private static DocTree docTree;
    /**
     * 差异列表
     */
    private BlockingQueue<DocDiff> docDiffs;

    @Resource
    private DocLockManager docLockManager;
    @Resource
    private FileSyncConfig fileSyncConfig;


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
                docDiffs.put(docDiff);
            } catch (InterruptedException e) {
                log.error("docDiffs添加失败, docDiff: {}", docDiff);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 初始化-文档资源树根节点
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        docDiffs = new ArrayBlockingQueue<>(1024);
        File rootDir = new File(fileSyncConfig.getRootPath());
        docTree = FileUtil.visitPath(rootDir, fileSyncConfig.getRootPath());
        log.info("文档树初始化成功, rootTree: {}", GsonUtil.toJsonString(docTree));
    }
}
