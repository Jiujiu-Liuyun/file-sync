package com.zhangyun.file.server.controller;

import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.dto.DownloadDocDiffDTO;
import com.zhangyun.file.common.domain.req.DownloadDocDiffReq;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.domain.resp.DownloadDocDiffResp;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.ObjectUtil;
import com.zhangyun.file.server.config.FileSyncConfig;
import com.zhangyun.file.server.remote.netty.service.NotifyService;
import com.zhangyun.file.server.service.ServerDocManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/doc/transfer")
@Api(tags = "文档传输")
public class DocTransferController {
    @Resource
    private ServerDocManageService serverDocManageService;
    @Resource
    private FileSyncConfig fileSyncConfig;
    @Resource
    private NotifyService notifyService;

    @PostMapping("/upload")
    @ApiOperation("upload")
    @Timer
    public BaseResp upload(@RequestBody UploadDocDiffReq req) {
        serverDocManageService.handleDocumentDiff(req);
        notifyService.notifyDocDiff(req.getDiff(), req.getDeviceId());
        return BaseResp.success();
    }

    @PostMapping("/download")
    @ApiOperation("download")
    @Timer
    public DownloadDocDiffResp download(@RequestBody DownloadDocDiffReq req) {
        String path = req.getDiff().getDocIdentity().getAbsolutePath(fileSyncConfig.getRootPath());
        String content = FileUtil.readFile(new File(path));
        DownloadDocDiffDTO data = new DownloadDocDiffDTO(req.getDiff(), content);
        return ObjectUtil.successResp(data, DownloadDocDiffResp.class);
    }

}
