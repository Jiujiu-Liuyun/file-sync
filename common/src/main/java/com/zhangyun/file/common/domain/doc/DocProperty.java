package com.zhangyun.file.common.domain.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocProperty implements Serializable {
    private Long lastModifyTime;

    public static DocProperty of(Path path) {
        File file = new File(path.toUri());
        return new DocProperty(file.lastModified());
    }

    public DocProperty deepCopy() {
        return new DocProperty(lastModifyTime);
    }
}
