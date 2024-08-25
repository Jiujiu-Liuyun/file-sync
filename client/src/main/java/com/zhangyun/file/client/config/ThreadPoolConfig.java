package com.zhangyun.file.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean
    public ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(3);
    }

}
