package com.zhangyun.file.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RespCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(300, "失败")
    ;

    private Integer code;
    private String desc;

    private static RespCodeEnum of(Integer code) {
        return Arrays.stream(RespCodeEnum.values())
                .filter(codeEnum -> codeEnum.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
