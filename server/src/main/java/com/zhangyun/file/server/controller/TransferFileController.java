package com.zhangyun.file.server.controller;

import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.server.service.ServerDocManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/file/transfer")
@Api(tags = "传输文件")
public class TransferFileController {
    @Resource
    private ServerDocManageService serverDocManageService;

    @PostMapping("/upload")
    @ApiOperation("upload")
    @Timer
    public BaseResp upload(@RequestBody UploadDocDiffReq req) {
        serverDocManageService.handleDocumentDiff(req);
        return BaseResp.success();
    }

}
