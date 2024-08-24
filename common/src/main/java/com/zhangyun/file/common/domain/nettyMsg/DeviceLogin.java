package com.zhangyun.file.common.domain.nettyMsg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogin extends BaseMsg {
    private String deviceId;

    @Override
    public int getMessageType() {
        return DEVICE_LOGIN;
    }
}
