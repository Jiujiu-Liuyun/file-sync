package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

@Data
@Builder
public class Doc {
    private DocIdentity docIdentity;
    private DocProperty docProperty;

    public static Doc of(Path path) {
        DocIdentity docIdentity = DocIdentity.of(path);
        DocProperty docProperty = DocProperty.of(path);
        return Doc.builder().docIdentity(docIdentity).docProperty(docProperty).build();
    }

    public Doc deepCopy() {
        return Doc.builder().docIdentity(docIdentity.deepCopy()).docProperty(docProperty.deepCopy()).build();
    }
}
