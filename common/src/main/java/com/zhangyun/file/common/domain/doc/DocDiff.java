package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Data;

@Data
public class DocDiff {
    private DocIdentity docIdentity;
    private DocumentDiffTypeEnum diffTypeEnum;
}
