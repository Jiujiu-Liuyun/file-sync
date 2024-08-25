package com.zhangyun.file.client.config;

import com.zhangyun.file.client.remote.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@Configuration
@Slf4j
public class NettyConfig {

    @Value("${netty.server.host}")
    private String host;
    @Value("${netty.server.port}")
    private Integer port;
    @Value("${netty.client.worker}")
    private Integer worker;

    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
}
