package com.zhangyun.file.common.domain.dto;

import com.zhangyun.file.common.domain.doc.DocDiff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadDocDiffDTO {
    private DocDiff diff;
    private byte[] content;
}
