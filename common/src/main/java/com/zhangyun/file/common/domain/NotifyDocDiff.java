package com.zhangyun.file.common.domain;

import com.zhangyun.file.common.domain.doc.DocumentDiff;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotifyDocDiff extends BaseMsg {
    private DocumentDiff diff;

    @Override
    public int getMessageType() {
        return NOTIFY_DOC_DIFF;
    }
}
