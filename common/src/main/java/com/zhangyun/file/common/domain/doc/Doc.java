package com.zhangyun.file.common.domain.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doc {
    private DocIdentity docIdentity;
    private DocProperty docProperty;

    public static Doc of(Path path, Path rootPath) {
        DocIdentity docIdentity = DocIdentity.of(path, rootPath);
        DocProperty docProperty = DocProperty.of(path);
        return new Doc(docIdentity, docProperty);
    }

    public Doc deepCopy() {
        return new Doc(docIdentity.deepCopy(), docProperty.deepCopy());
    }
}
