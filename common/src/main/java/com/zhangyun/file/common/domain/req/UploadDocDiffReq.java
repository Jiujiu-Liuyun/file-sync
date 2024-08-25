package com.zhangyun.file.common.domain.req;

import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocDiffReq {
    private DocDiff diff;
    private byte[] content;
    private String deviceId;
}
