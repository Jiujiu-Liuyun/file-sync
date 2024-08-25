package com.zhangyun.file.client.domain.doc;

import com.zhangyun.file.common.domain.doc.DocIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class DocLockManager {
    private final ReentrantLock docTreeLock;
    private final ConcurrentHashMap<String, ReentrantLock> docResourceMap;

    public DocLockManager() {
        docResourceMap = new ConcurrentHashMap<>();
        docTreeLock = new ReentrantLock(true);
    }

    public void lockDocTree() {
        docTreeLock.lock();
    }

    public void unlockDocTree() {
        docTreeLock.unlock();
    }

    public void lockDoc(DocIdentity docIdentity) {
        log.info("锁住文档资源, docIdentity: {}", docIdentity);
        ReentrantLock reentrantLock = new ReentrantLock(true);
        ReentrantLock lock = docResourceMap.putIfAbsent(docIdentity.getRelativePath(), reentrantLock);
        if (lock == null) {
            lock = reentrantLock;
        }
        lock.lock();
    }

    public void unlockDoc(DocIdentity docIdentity) {
        log.info("释放文档资源, docIdentity: {}", docIdentity);
        ReentrantLock lock = docResourceMap.get(docIdentity.getRelativePath());
        if (Objects.isNull(lock)) {
            throw new RuntimeException("文档资源锁不存在");
        }
        lock.unlock();
    }
}
