package com.zhangyun.file.common.domain.req;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocDiffReq extends BaseMsg {
    private DocumentDiff diff;
    private String content;

    @Override
    public int getMessageType() {
        return UPLOAD_DOCUMENT_DIFF_REQ;
    }
}
