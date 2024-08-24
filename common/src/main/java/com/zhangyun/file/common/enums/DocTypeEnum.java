package com.zhangyun.file.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocTypeEnum {
    FILE(1, "file"),
    DIR(2, "dir"),
    ;

    private final Integer type;
    private final String desc;

    private static DocTypeEnum of(Integer type) {
        return Arrays.stream(DocTypeEnum.values())
                .filter(typeEnum -> typeEnum.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
