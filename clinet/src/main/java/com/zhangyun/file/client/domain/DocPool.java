package com.zhangyun.file.client.domain;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.common.domain.doc.Doc;
import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 文档资源池
 */
@Component
@Slf4j
public class DocPool {
    @Resource
    private DocResourcePool docResourcePool;
    @Resource
    private FileSyncConfig fileSyncConfig;

    /**
     * 文档资源
     */
    private LinkedHashMap<String, Doc> docMap;
    /**
     * 差异列表
     */
    private ConcurrentLinkedDeque<DocDiff> docDiffs;

    public void init() {
        docMap = new LinkedHashMap<>();
    }

    public void addDoc(Doc doc) {
        docMap.put(doc.getDocIdentity().getRelativePath(), doc);
    }

    public void removeDoc(Doc doc) {
        docMap.remove(doc.getDocIdentity().getRelativePath());
    }

    public void addDocDiff(DocDiff diff) {
        docDiffs.addLast(diff);
    }

    public void removeDocDiff(DocDiff diff) {
        docDiffs.removeFirst();
    }
}