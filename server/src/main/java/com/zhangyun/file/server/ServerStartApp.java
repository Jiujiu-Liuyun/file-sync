package com.zhangyun.file.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(scanBasePackages = "com.zhangyun.file")
@Slf4j
@EnableOpenApi
@MapperScan("com.zhangyun.file.server.db.mapper")
public class ServerStartApp {

    public static void main(String[] args) {
        SpringApplication.run(ServerStartApp.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>> 服务启动成功 <<<<<<<<<<<<<<<<<<<<<<");
    }
}