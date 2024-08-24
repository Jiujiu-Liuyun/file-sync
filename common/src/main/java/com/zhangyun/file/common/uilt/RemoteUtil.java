package com.zhangyun.file.common.uilt;

import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.enums.RespCodeEnum;

public class RemoteUtil {

    public static void checkResp(BaseResp baseResp) {
        if (ObjectUtil.anyBlank(baseResp, baseResp.getCode(), baseResp.getMsg())) {
            throw new RuntimeException("远程调用错误，返回空");
        }
        if (!RespCodeEnum.SUCCESS.getCode().equals(baseResp.getCode())) {
            throw new RuntimeException(baseResp.getMsg());
        }
    }
}
