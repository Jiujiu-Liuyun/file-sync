package com.zhangyun.file.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FileSyncConfig {

    @Value("${file-sync.server.path}")
    private String rootPath;

}
