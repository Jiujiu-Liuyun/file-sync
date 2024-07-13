package com.zhangyun.file.client.config;

import com.zhangyun.file.client.handler.ClientHandler;
import com.zhangyun.file.common.domain.doc.Document;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@Configuration
public class NettyConfig {

    @Value("${netty.server.host}")
    private String host;

    @Value("${netty.server.port}")
    private Integer port;

    @Resource
    private ClientHandler clientHandler;

    /**
     * 客户端启动器
     */
    @Bean
    public Bootstrap bootstrap(@Value("${netty.client.worker}") Integer worker){
        // 新建一组线程池
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(worker);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(eventExecutors)   // 指定线程组
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class) // 指定通道
                .handler(clientHandler); // 指定处理器
        return bootstrap;
    }
}
