package com.zhangyun.file.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocDiffTypeEnum {
    CREATE(1, "create"),
    CHANGE(2, "change"),
    DELETE(3, "delete"),
    ;

    private Integer type;
    private String desc;

    private static DocDiffTypeEnum of(Integer type) {
        return Arrays.stream(DocDiffTypeEnum.values())
                .filter(typeEnum -> typeEnum.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
