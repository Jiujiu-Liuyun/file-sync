package com.zhangyun.file.common.domain.nettyMsg;

import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDocDiff extends BaseMsg {
    private DocDiff diff;

    @Override
    public int getMessageType() {
        return NOTIFY_DOC_DIFF;
    }
}
