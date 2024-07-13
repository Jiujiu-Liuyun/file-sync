package com.zhangyun.file.common.domain.resp;

import com.zhangyun.file.common.domain.BaseMsg;
import com.zhangyun.file.common.enums.RespCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResp extends BaseMsg {
    private Integer code;
    private String errMsg;

    public static BaseResp success() {
        return new BaseResp(RespCodeEnum.SUCCESS.getCode(), "");
    }

    public static BaseResp fail() {
        return new BaseResp(RespCodeEnum.FAIL.getCode(), "系统异常");
    }

    public static BaseResp fail(String errMsg) {
        return new BaseResp(RespCodeEnum.FAIL.getCode(), errMsg);
    }

    @Override
    public int getMessageType() {
        return 1;
    }
}
