package com.zhangyun.file.server.config;

import com.zhangyun.file.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@Configuration
public class NettyConfig {

    @Value("${netty.port}")
    private Integer port;

    @Resource
    private ServerHandler serverHandler;

    /**
     * 服务器启动器
     *
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap(@Value("${netty.boss}") Integer boss,
                                           @Value("${netty.worker}") Integer worker,
                                           @Value("${netty.timeout}") Integer timeout) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(new NioEventLoopGroup(boss), new NioEventLoopGroup(worker))   // 指定使用的线程组
                .channel(NioServerSocketChannel.class) // 指定使用的通道
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout) // 指定连接超时时间
                .childHandler(serverHandler); // 指定worker处理器
        return serverBootstrap;
    }
}
