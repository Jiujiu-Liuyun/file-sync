package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocIdentity {
    private String name;
    private DocumentTypeEnum typeEnum;


}
