package com.zhangyun.file.common.domain.req;

import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadDocDiffReq {
    private DocDiff diff;
}
