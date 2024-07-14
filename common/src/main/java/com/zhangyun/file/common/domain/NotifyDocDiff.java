package com.zhangyun.file.common.domain;

import com.zhangyun.file.common.domain.doc.DocumentDiff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDocDiff extends BaseMsg {
    private DocumentDiff diff;

    @Override
    public int getMessageType() {
        return NOTIFY_DOC_DIFF;
    }
}
