package com.zhangyun.file.common.domain.req;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFileReq extends BaseMsg {
    private DocDiffTypeEnum diffTypeEnum;
    private String name;
    private String relativePath;
    private DocumentTypeEnum typeEnum;

    @Override
    public int getMessageType() {
        return DOWNLOAD_FILE_REQ;
    }
}
