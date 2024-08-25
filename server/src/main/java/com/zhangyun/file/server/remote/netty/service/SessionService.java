package com.zhangyun.file.server.remote.netty.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SessionService {
    private Map<String, Channel> deviceToChannel;
    private Map<Channel, String> channelToDevice;

    public SessionService() {
        deviceToChannel = new HashMap<>();
        channelToDevice = new HashMap<>();
    }

    public boolean isOnline(String deviceId) {
        return deviceToChannel.containsKey(deviceId);
    }

    public void login(String deviceId, Channel channel) {
        log.info("设备登录, deviceId: {}, channel: {}", deviceId, channel);
        deviceToChannel.put(deviceId, channel);
        channelToDevice.put(channel, deviceId);
    }

    public void logout(Channel channel) {
        log.info("设备下线, channel: {}", channel);
        String deviceId = channelToDevice.get(channel);
        channelToDevice.remove(channel);
        deviceToChannel.remove(deviceId);
    }

    public Map<String, Channel> getOtherSession(String deviceId) {
        Map<String, Channel> other = new HashMap<>(deviceToChannel);
        other.remove(deviceId);
        return other;
    }
}
