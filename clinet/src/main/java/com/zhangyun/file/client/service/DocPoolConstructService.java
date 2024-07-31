package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.domain.DocPool;
import com.zhangyun.file.client.domain.DocResourcePool;
import com.zhangyun.file.common.domain.doc.Doc;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

@Getter
@Slf4j
@Service
public class DocPoolConstructService implements FileVisitor<Path>, InitializingBean {
    @Resource
    private DocPool docPool;
    @Resource
    private DocResourcePool docResourcePool;
    @Resource
    private FileSyncConfig fileSyncConfig;


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Doc doc = Doc.of(dir);
        docPool.addDoc(doc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Doc doc = Doc.of(file);
        docPool.addDoc(doc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        log.error("【DocConstructVisitor.visitFileFailed】访问失败, err:{}", ExceptionUtils.getStackTrace(exc));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (Objects.nonNull(exc)) {
            log.error("【DocConstructVisitor.postVisitDirectory】访问失败, err:{}", ExceptionUtils.getStackTrace(exc));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        docResourcePool.lockPool();
        try {
            Files.walkFileTree(Paths.get(fileSyncConfig.getPath()), this);
        } finally {
            docResourcePool.unlockPool();
        }
    }
}
