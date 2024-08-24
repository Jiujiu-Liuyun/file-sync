package com.zhangyun.file.server.service;

import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.server.config.FileSyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@Service
public class ServerDocManageService {
    @Resource
    private FileSyncConfig config;

    public void handleDocumentDiff(UploadDocDiffReq req) {
        DocumentDiff diff = req.getDiff();
        String path = diff.getDocAbsolutePath(config.getPath());
        File file = new File(path);
        if (diff.getTypeEnum() == DocumentTypeEnum.FILE && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            FileUtil.writeFile(req.getContent(), file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.FILE) {
            FileUtil.deleteFile(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.CREATE) {
            FileUtil.createDir(file);
        } else if (diff.getTypeEnum() == DocumentTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            FileUtil.deleteDir(file);
        }
    }

}
