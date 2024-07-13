package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;

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
    private DocumentTypeEnum typeEnum;
    private Long lastModifyTime;
    private List<Document> subDocuments;

    public String getAbsolutePath(String rootPath) {
        return Paths.get(rootPath, relativePath).toString();
    }

    public DocIdentity getDocIdentity() {
        return DocIdentity.builder().name(name).typeEnum(typeEnum).build();
    }

    public Map<DocIdentity, Document> getSubDocIdenMap() {
        if (CollectionUtils.isEmpty(subDocuments)) {
            return new HashMap<>();
        }
        return subDocuments.stream().collect(Collectors.toMap(Document::getDocIdentity, Function.identity()));
    }

    public boolean isFile() {
        return typeEnum == DocumentTypeEnum.FILE;
    }

    public boolean isDir() {
        return typeEnum == DocumentTypeEnum.DIR;
    }


}
