package com.zhangyun.file.common.domain.doc;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.nio.file.Path;

@Data
@Builder
public class DocProperty {
    private Long lastModifyTime;

    public static DocProperty of(Path path) {
        File file = new File(path.toUri());
        return DocProperty.builder().lastModifyTime(file.lastModified()).build();
    }

    public DocProperty deepCopy() {
        return DocProperty.builder().lastModifyTime(lastModifyTime).build();
    }
}
