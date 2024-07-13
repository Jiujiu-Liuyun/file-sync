package com.zhangyun.file.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentDiffTypeEnum {
    CREATE(1, "create"),
    CHANGE(2, "change"),
    DELETE(3, "delete"),
    ;

    private Integer type;
    private String desc;

    private static DocumentDiffTypeEnum of(Integer type) {
        return Arrays.stream(DocumentDiffTypeEnum.values())
                .filter(typeEnum -> typeEnum.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
