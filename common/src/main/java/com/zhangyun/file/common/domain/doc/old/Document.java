package com.zhangyun.file.common.domain.doc.old;

import com.zhangyun.file.common.enums.DocTypeEnum;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
public class Document {
    private String name;
    private String relativePath;
    private DocTypeEnum typeEnum;
    private Long lastModifyTime;
    private List<Document> subDocuments;

    public String getAbsolutePath(String rootPath) {
        return Paths.get(rootPath, relativePath).toString();
    }

    public DocIdentityV1 getDocIdentity() {
        return DocIdentityV1.builder().name(name).typeEnum(typeEnum).build();
    }

    public Map<DocIdentityV1, Document> getSubDocIdenMap() {
        if (CollectionUtils.isEmpty(subDocuments)) {
            return new HashMap<>();
        }
        return subDocuments.stream().collect(Collectors.toMap(Document::getDocIdentity, Function.identity()));
    }

    public boolean isFile() {
        return typeEnum == DocTypeEnum.FILE;
    }

    public boolean isDir() {
        return typeEnum == DocTypeEnum.DIR;
    }


}
