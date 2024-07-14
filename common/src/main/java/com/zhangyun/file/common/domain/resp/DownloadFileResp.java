package com.zhangyun.file.common.domain.resp;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadFileResp extends BaseMsg {
    private DocumentDiffTypeEnum diffTypeEnum;
    private String name;
    private String relativePath;
    private DocumentTypeEnum typeEnum;
    private String content;
    
    @Override
    public int getMessageType() {
        return DOWNLOAD_FILE_RESP;
    }
}
