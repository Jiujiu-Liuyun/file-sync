package com.zhangyun.file.common.domain.resp;

import com.zhangyun.file.common.enums.RespCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResp {
    private Integer code;
    private String msg;

    public static BaseResp success() {
        return new BaseResp(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getDesc());
    }

    public static BaseResp fail() {
        return new BaseResp(RespCodeEnum.FAIL.getCode(), RespCodeEnum.FAIL.getDesc());
    }

    public static BaseResp fail(String msg) {
        return new BaseResp(RespCodeEnum.FAIL.getCode(), msg);
    }
}
