package com.zhangyun.file.client.util;

import com.zhangyun.file.client.service.DocCompareService;
import com.zhangyun.file.client.service.DocPoolConstructService;
import com.zhangyun.file.common.domain.doc.Doc;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.uilt.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DocCompareVisitorTest {
    public static Map<String, Doc> docMap;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\test");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        DocPoolConstructService docPoolConstructService = new DocPoolConstructService();
        Files.walkFileTree(path, docPoolConstructService);
        docMap = docPoolConstructService.getDocMap();

        Runnable runnable = () -> {
            DocCompareService docCompareService = new DocCompareService(docMap, new HashSet<>());
            try {
                Files.walkFileTree(path, docCompareService);
                List<DocDiff> docDiffs = docCompareService.getDocDiffs();
                log.info("docDiffs: {}", GsonUtil.toJsonString(docDiffs));
                docMap = docCompareService.getNewDocMap();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 2, 2, TimeUnit.SECONDS);
    }

}