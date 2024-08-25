package com.zhangyun.file.client.domain.doc.service;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.remote.service.RemoteNettyService;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.req.DownloadDocDiffReq;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.domain.resp.DownloadDocDiffResp;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.RemoteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;

/**
 * 文档传输服务
 */
@Service
@Slf4j
public class DocTransferService {
    @Resource
    private RemoteNettyService remoteNettyService;
    @Resource
    private FileSyncConfig fileSyncConfig;
    @Resource
    private RestTemplate restTemplate;

    public void uploadDocDiff(DocDiff diff) {
        if (diff == null) {
            log.error("上传文档差异列表, diff is null");
            return;
        }
        String content = null;
        if (diff.getDocIdentity().getDocTypeEnum() == DocTypeEnum.FILE
                && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            String absolutePath = diff.getDocIdentity().getAbsolutePath(fileSyncConfig.getRootPath());
            content = FileUtil.readFile(new File(absolutePath));
        }
        UploadDocDiffReq req = new UploadDocDiffReq(diff, content, fileSyncConfig.getDeviceId());
        BaseResp resp = restTemplate.postForObject("http://" + fileSyncConfig.getServerHost() + ":" + fileSyncConfig.getServerPort() + "/doc/transfer/upload", req, BaseResp.class);
        RemoteUtil.checkResp(resp);
    }

    public void downloadDocDiff(DocDiff diff) {
        String path = diff.getDocIdentity().getAbsolutePath(fileSyncConfig.getRootPath());
        File file = new File(path);
        if (diff.getDocIdentity().getDocTypeEnum() == DocTypeEnum.FILE
                && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            // 请求下载文件
            DownloadDocDiffReq req = new DownloadDocDiffReq(diff);
            DownloadDocDiffResp resp = restTemplate.postForObject("http://" + fileSyncConfig.getServerHost() + ":" + fileSyncConfig.getServerPort() + "/doc/transfer/download", req, DownloadDocDiffResp.class);
            RemoteUtil.checkResp(resp);
            FileUtil.writeFile(resp.getData().getContent(), file);
        } else if (diff.getDocIdentity().getDocTypeEnum() == DocTypeEnum.FILE
                && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            FileUtil.deleteFile(file);
        } else if (diff.getDocIdentity().getDocTypeEnum() == DocTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.CREATE) {
            FileUtil.createDir(file);
        } else if (diff.getDocIdentity().getDocTypeEnum() == DocTypeEnum.DIR && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            FileUtil.deleteDir(file);
        }
    }
}
