package com.zhangyun.file.common.domain.doc.old;

import com.zhangyun.file.common.enums.DocTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocIdentityV1 {
    private String name;
    private DocTypeEnum typeEnum;


}
