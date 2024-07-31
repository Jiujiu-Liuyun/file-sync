package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Data
@Builder
public class DocIdentity {
    private String name;
    private String parentPath;
    private DocumentTypeEnum typeEnum;

    public static DocIdentity of(Path path) {
        File file = new File(path.toUri());
        return DocIdentity.builder().name(file.getName())
                .parentPath(file.getParent())
                .typeEnum(file.isFile() ? DocumentTypeEnum.FILE : DocumentTypeEnum.DIR)
                .build();
    }

    public String getRelativePath() {
        return Paths.get(parentPath, name).toString();
    }

    public String getAbsolutePath(String rootPath) {
        return Paths.get(rootPath, parentPath, name).toString();
    }

    public DocIdentity deepCopy() {
        return DocIdentity.builder()
                .name(name)
                .parentPath(parentPath)
                .typeEnum(typeEnum).build();
    }
}
