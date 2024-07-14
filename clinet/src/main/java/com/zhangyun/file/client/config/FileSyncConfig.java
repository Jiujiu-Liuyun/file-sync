package com.zhangyun.file.client.config;

import com.zhangyun.file.common.domain.doc.Document;
import com.zhangyun.file.common.domain.doc.DocumentDiff;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Data
@Configuration
public class FileSyncConfig {

    @Value("${file-sync.client.path}")
    private String path;

    @Value("${file-sync.client.interval}")
    private Integer interval;

    @Value("${file-sync.server.host}")
    private String serverHost;

    @Value("${file-sync.server.port}")
    private String serverPort;

    // 用户同步目录
    private Document rootDocument;

    @Bean
    public BlockingQueue<DocumentDiff> initDocDiffBlockingQueue() {
        return new ArrayBlockingQueue<>(500);
    }

    @Bean
    public Set<DocumentDiff> ignoreDocDiffSet() {
        return new HashSet<>();
    }
}
