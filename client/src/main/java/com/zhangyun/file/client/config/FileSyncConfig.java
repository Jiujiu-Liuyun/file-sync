package com.zhangyun.file.client.config;

import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.doc.old.Document;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Data
@Configuration
public class FileSyncConfig {

    @Value("${file-sync.client.path}")
    private String rootPath;

    @Value("${file-sync.client.deviceId}")
    private String deviceId;

    @Value("${file-sync.client.interval}")
    private Integer interval;

    @Value("${file-sync.server.host}")
    private String serverHost;

    @Value("${file-sync.server.port}")
    private String serverPort;

    // 用户同步目录
    private Document rootDocument;

    @Bean(name = "remoteDocDiffQueue")
    public BlockingQueue<DocDiff> initDocDiffBlockingQueue() {
        return new ArrayBlockingQueue<>(1024);
    }

    @Bean(name = "ignoreDocDiffSet")
    public Set<DocumentDiff> ignoreDocDiffSet() {
        return new HashSet<>();
    }
}
