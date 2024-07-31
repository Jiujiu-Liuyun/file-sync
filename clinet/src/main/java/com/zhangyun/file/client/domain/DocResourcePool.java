package com.zhangyun.file.client.domain;

import com.zhangyun.file.common.domain.doc.DocIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class DocResourcePool {
    private final ReentrantLock poolLock;
    private final ConcurrentHashMap<String, ReentrantLock> docResourceMap;

    public DocResourcePool() {
        docResourceMap = new ConcurrentHashMap<>();
        poolLock = new ReentrantLock(true);
    }

    public void lockPool() {
        log.info("锁住资源池");
        poolLock.lock();
    }

    public void unlockPool() {
        log.info("释放资源池");
        poolLock.unlock();
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
