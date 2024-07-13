package com.zhangyun.file.client.config;

import com.zhangyun.file.common.domain.doc.Document;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

}
