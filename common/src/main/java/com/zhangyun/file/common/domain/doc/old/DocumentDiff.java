package com.zhangyun.file.common.domain.doc.old;

import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDiff implements Serializable {
    private DocDiffTypeEnum diffTypeEnum;
    private String name;
    private String relativePath;
    private DocTypeEnum typeEnum;

    public DocumentDiff(Document doc, DocDiffTypeEnum typeEnum) {
        this.diffTypeEnum = typeEnum;
        if (doc != null) {
            this.name = doc.getName();
            this.relativePath = doc.getRelativePath();
            this.typeEnum = doc.getTypeEnum();
        }
    }

    public String getDocAbsolutePath(String rootPath) {
        return Paths.get(rootPath, relativePath).toString();
    }
}
