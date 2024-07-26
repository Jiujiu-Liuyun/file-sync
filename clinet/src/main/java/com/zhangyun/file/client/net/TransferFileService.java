package com.zhangyun.file.client.net;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.service.ClientNettyService;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import com.zhangyun.file.common.enums.RespCodeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.Objects;

@Service
@Slf4j
public class TransferFileService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private FileSyncConfig config;

    @Resource
    private ClientNettyService clientNettyService;

    public void uploadDocumentDiff(DocumentDiff diff) {
        String path = diff.getDocAbsolutePath(config.getPath());
        String content = null;
        if (diff.getTypeEnum() == DocumentTypeEnum.FILE && diff.getDiffTypeEnum() != DocumentDiffTypeEnum.DELETE) {
            content = FileUtil.readFile(new File(path));
        }
        UploadDocDiffReq req = new UploadDocDiffReq(diff, content);
        BaseResp baseResp = restTemplate.postForObject("http://" + config.getServerHost() + ":" + config.getServerPort() + "/file/transfer/upload", req, BaseResp.class);
        if (!Objects.equals(baseResp.getCode(), RespCodeEnum.SUCCESS.getCode())) {
            throw new RuntimeException(baseResp.getErrMsg());
        }
    }

    public void uploadDocumentDiffNetty(DocumentDiff diff) {
        String path = diff.getDocAbsolutePath(config.getPath());
        String content = null;
        if (diff.getTypeEnum() == DocumentTypeEnum.FILE && diff.getDiffTypeEnum() != DocumentDiffTypeEnum.DELETE) {
            content = FileUtil.readFile(new File(path));
        }
        UploadDocDiffReq req = new UploadDocDiffReq(diff, content);
//        BaseResp baseResp = restTemplate.postForObject("http://" + config.getServerHost() + ":" + config.getServerPort() + "/file/transfer/upload", req, BaseResp.class);
//        if (!Objects.equals(baseResp.getCode(), RespCodeEnum.SUCCESS.getCode())) {
//            throw new RuntimeException(baseResp.getErrMsg());
//        }
        clientNettyService.sendMessage(req);
    }

}
