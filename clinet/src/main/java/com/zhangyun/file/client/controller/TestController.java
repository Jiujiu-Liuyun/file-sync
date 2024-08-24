package com.zhangyun.file.client.controller;

import com.zhangyun.file.client.config.FileSyncConfig;
import com.zhangyun.file.client.domain.doc.DocManager;
import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.doc.DocTree;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Paths;

@Slf4j
@RestController
@Api(tags = "测试")
public class TestController {
    @Resource
    private DocManager docManager;
    @Resource
    private FileSyncConfig fileSyncConfig;

    @GetMapping("/test")
    @ApiOperation("test")
    @Timer
    public String test() {
        return "test success!";
    }

    @GetMapping("/compare")
    @ApiOperation("compare")
    @Timer
    public String compare() {
        docManager.compareDocTree();
        return "success";
    }

    @GetMapping("/updateTreeNode")
    @ApiOperation("updateTreeNode")
    @Timer
    public String updateTreeNode(String absolutePath, DocDiffTypeEnum diffTypeEnum) {
        DocTree docTree = docManager.getDocTree();
        DocTree.updateTreeNode(docTree, Paths.get(fileSyncConfig.getRootPath()).relativize(Paths.get(absolutePath)), Paths.get(fileSyncConfig.getRootPath()), diffTypeEnum);
        return GsonUtil.toJsonString(docTree);
    }



}
