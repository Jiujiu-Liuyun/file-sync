package com.zhangyun.file.server.service;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.common.uilt.GsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class SessionService {
    private Set<Channel> channels;

    public SessionService() {
        channels = new HashSet<>();
    }

    public boolean isOnline(Channel channel) {
        return channels.contains(channel);
    }

    public void register(Channel channel) {
        log.info("通道注册, channel: {}", channel);
        channels.add(channel);
    }

    public void offline(Channel channel) {
        log.info("通道下线, channel: {}", channel);
        channels.remove(channel);
    }

    private Set<Channel> getOtherSession(Channel channel) {
        HashSet<Channel> other = new HashSet<>(channels);
        other.remove(channel);
        return other;
    }

    public void sendMsg2OtherChannel(Channel channel, BaseMsg msg) {
        Set<Channel> otherSession = getOtherSession(channel);
        otherSession.forEach(ch -> ch.writeAndFlush(msg));
    }
}
