package com.zhangyun.file.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentTypeEnum {
    FILE(1, "file"),
    DIR(2, "dir"),
    ;

    private Integer type;
    private String desc;

    private static DocumentTypeEnum of(Integer type) {
        return Arrays.stream(DocumentTypeEnum.values())
                .filter(typeEnum -> typeEnum.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
