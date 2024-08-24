package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocIdentity {
    private String name;
    private String parentPath;
    private DocumentTypeEnum typeEnum;

    public static DocIdentity of(Path path, Path rootPath) {
        File file = new File(path.toUri());
        return new DocIdentity(file.getName(), getRelativePath(file.getParentFile().toPath(), rootPath),
                file.isFile() ? DocumentTypeEnum.FILE : DocumentTypeEnum.DIR);
    }

    public static String getRelativePath(Path path, Path rootPath) {
        Path relativize = rootPath.relativize(path);
        return relativize.toString();
    }

    public String getRelativePath() {
        return Paths.get(parentPath, name).toString();
    }

    public String getAbsolutePath(String rootPath) {
        return Paths.get(rootPath, parentPath, name).toString();
    }

    public DocIdentity deepCopy() {
        return new DocIdentity(name, parentPath, typeEnum);
    }
}
