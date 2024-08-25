package com.zhangyun.file.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class, scanBasePackages = "com.zhangyun.file")
@Slf4j
@EnableOpenApi
public class ClientStartApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientStartApp.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>> 服务启动成功 <<<<<<<<<<<<<<<<<<<<<<");
    }
}