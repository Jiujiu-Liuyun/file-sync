package com.zhangyun.file.client.domain;

import com.zhangyun.file.common.domain.doc.Doc;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  文档资源
 */
@Component
public class DocResource {
    private Map<String, Doc> docMap;

    public DocResource() {
        docMap = new ConcurrentHashMap<>();
    }

    public void addDoc(Doc doc) {
        docMap.put(doc.getDocIdentity().getRelativePath(), doc);
    }

    public void removeDoc(Doc doc) {
        docMap.remove(doc.getDocIdentity().getRelativePath());
    }


}
