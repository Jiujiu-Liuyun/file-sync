package com.zhangyun.file.server.remote.netty.service;

import com.zhangyun.file.common.domain.nettyMsg.BaseMsg;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class SessionService {
    private Map<String, Channel> deviceSessions;

    public SessionService() {
        deviceSessions = new HashMap<>();
    }

    public boolean isOnline(String deviceId) {
        return deviceSessions.containsKey(deviceId);
    }

    public void login(String deviceId, Channel channel) {
        log.info("设备登录, deviceId: {}, channel: {}", deviceId, channel);
        deviceSessions.put(deviceId, channel);
    }

    public void logout(String deviceId) {
        log.info("设备下线, deviceId: {}", deviceId);
        deviceSessions.remove(deviceId);
    }

    public Map<String, Channel> getOtherSession(String deviceId) {
        Map<String, Channel> other = new HashMap<>(deviceSessions);
        other.remove(deviceId);
        return other;
    }
}
