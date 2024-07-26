package com.zhangyun.file.client.util;

import com.zhangyun.file.common.domain.doc.Doc;
import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/**
 * 文档递归比较
 */
@Slf4j
public class DocCompareVisitor implements FileVisitor<Path> {
    private Map<String, Doc> docMap;
    private List<DocDiff> docDiffs;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        log.info("path: {}, attr: {}", dir, attrs.lastModifiedTime());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        log.info("path: {}, attr: {}", file, attrs.lastModifiedTime());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        log.info("path: {}", file, exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        log.info("path: {}", dir, exc);
        return FileVisitResult.CONTINUE;
    }
}
