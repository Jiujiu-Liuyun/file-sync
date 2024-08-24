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
public class NettyConfig implements InitializingBean {

    @Value("${netty.server.host}")
    private String host;
    @Value("${netty.server.port}")
    private Integer port;
    @Value("${netty.client.worker}")
    private Integer worker;

    @Resource
    private ClientHandler clientHandler;

    @Getter
    private ChannelFuture channelFuture;

    /**
     * netty客户端 channel
     */
    public void initNetty() {
        try {
            // 新建一组线程池
            NioEventLoopGroup eventExecutors = new NioEventLoopGroup(worker);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(eventExecutors)   // 指定线程组
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class) // 指定通道
                    .handler(clientHandler); // 指定处理器
            channelFuture = bootstrap.connect(host, port).sync();
            log.info("netty客户端启动成功, channelFuture: {}", channelFuture);
        } catch (Exception e) {
            log.error("netty客户端启动失败, err: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initNetty();
    }
}
