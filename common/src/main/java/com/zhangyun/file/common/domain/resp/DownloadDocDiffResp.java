package com.zhangyun.file.common.domain.resp;

import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.dto.DownloadDocDiffDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class DownloadDocDiffResp extends BaseResp {
    private DownloadDocDiffDTO data;
}
