package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class Doc {
    private DocIdentity docIdentity;
    private Long lastModifyTime;
}
