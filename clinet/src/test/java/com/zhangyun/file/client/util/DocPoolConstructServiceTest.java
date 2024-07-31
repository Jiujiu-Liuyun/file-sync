package com.zhangyun.file.client.util;

import com.zhangyun.file.client.service.DocPoolConstructService;
import com.zhangyun.file.common.uilt.GsonUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocPoolConstructServiceTest {

    @Test
    public void test() throws IOException {
        DocPoolConstructService visitor = new DocPoolConstructService();
        Files.walkFileTree(Paths.get("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1"), visitor);
        System.out.println(GsonUtil.toJsonString(visitor.getDocMap()));
    }
}