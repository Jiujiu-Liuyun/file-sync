package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Data;

import java.nio.file.Paths;

@Data
public class DocIdentity {
    private String name;
    private String parentPath;
    private DocumentTypeEnum typeEnum;

    public String getRelativePath() {
        return Paths.get(parentPath, name).toString();
    }

    public String getAbsolutePath(String rootPath) {
        return Paths.get(rootPath, parentPath, name).toString();
    }
}
