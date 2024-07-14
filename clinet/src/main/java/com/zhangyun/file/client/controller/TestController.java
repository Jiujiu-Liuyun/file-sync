package com.zhangyun.file.client.controller;

import com.zhangyun.file.common.annotation.Timer;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import com.zhangyun.file.common.uilt.FileUtil;
import com.zhangyun.file.common.uilt.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;

@Slf4j
@RestController
@Api(tags = "测试")
public class TestController {

    @GetMapping("/test")
    @ApiOperation("test")
    @Timer
    public String test() {
        return "test success!";
    }

    @GetMapping("/resolvePath")
    @ApiOperation("resolvePath")
    @Timer
    public String resolvePath(@RequestParam("path") String path) {
        return GsonUtil.toJsonString(FileUtil.recursionPath(new File(path), "C:\\"));
    }

//    @GetMapping("/comparePath")
//    @ApiOperation("comparePath")
//    @Timer
//    public String comparePath(@RequestParam("path1") String path1, @RequestParam("path2") String path2) {
//        ArrayList<DocumentDiff> documentDiffList = new ArrayList<>();
//        FileUtil.compareDocument(FileUtil.recursionPath(new File(path1), "C:\\"), FileUtil.recursionPath(new File(path2), "C:\\"), documentDiffList, true);
//        String content = GsonUtil.toJsonString(documentDiffList);
//        FileUtil.writeJsonStr2File(content, new File("tmp.json"));
//        return content;
//    }


}
