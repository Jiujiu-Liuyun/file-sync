package com.zhangyun.file.client.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.domain.DocPool;
import com.zhangyun.file.client.domain.DocResourcePool;
import com.zhangyun.file.common.domain.doc.Doc;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 文档递归比较
 */
@Slf4j
@Getter
@Service
public class DocCompareService implements FileVisitor<Path> {
    @Resource
    private DocPool docPool;
    @Resource
    private DocResourcePool docResourcePool;
    @Resource
    private FileSyncConfig fileSyncConfig;

    private LinkedHashMap<String, Doc> newDocMap;

    public void docCompare() {
        docResourcePool.lockPool();
        try {
            newDocMap = new LinkedHashMap<>();
            Files.walkFileTree(Paths.get(fileSyncConfig.getPath()), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            docResourcePool.unlockPool();
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Doc newDoc = Doc.of(dir);
        newDocMap.put(newDoc.getDocIdentity().getRelativePath(), newDoc);
        DocDiff diff = buildDocDiff(dir);
        if (diff != null && diff.getDiffTypeEnum() == DocumentDiffTypeEnum.CREATE) {
            docDiffs.addLast(diff);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Doc newDoc = Doc.of(file);
        newDocMap.put(newDoc.getDocIdentity().getRelativePath(), newDoc);
        DocDiff diff = buildDocDiff(file);
        if (diff != null) {
            docDiffs.addLast(diff);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        log.error("【DocCompareVisitor.visitFileFailed】访问失败, err:{}", ExceptionUtils.getStackTrace(exc));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (Objects.nonNull(exc)) {
            log.error("【DocCompareVisitor.postVisitDirectory】访问失败, err:{}", ExceptionUtils.getStackTrace(exc));
        }
        DocDiff diff = buildDocDiff(dir);
        if (diff != null && diff.getDiffTypeEnum() == DocumentDiffTypeEnum.DELETE) {
            docDiffs.addLast(diff);
        }
        return FileVisitResult.CONTINUE;
    }

    private DocDiff buildDocDiff(Path dir) {
        Doc newDoc = Doc.of(dir);
        Doc old = docMap.get(newDoc.getDocIdentity().getRelativePath());
        return DocDiff.of(old, newDoc);
    }
}
