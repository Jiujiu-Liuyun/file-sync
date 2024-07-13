package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
public class DocumentDiff implements Serializable {
    private DocumentDiffTypeEnum diffTypeEnum;
    private String name;
    private String relativePath;
    private DocumentTypeEnum typeEnum;
    private Long lastModifyTime;

    public DocumentDiff(Document doc, DocumentDiffTypeEnum typeEnum) {
        this.diffTypeEnum = typeEnum;
        if (doc != null) {
            this.name = doc.getName();
            this.relativePath = doc.getRelativePath();
            this.typeEnum = doc.getTypeEnum();
            this.lastModifyTime = doc.getLastModifyTime();
        }
    }

    public String getDocAbsolutePath(String rootPath) {
        return Paths.get(rootPath, relativePath).toString();
    }
}
