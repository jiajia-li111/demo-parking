package com.tree.plms.enums;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    ENABLED("01", "启用"),
    DISABLED("02", "禁用");

    private final String code;
    private final String desc;

    public static String getDescByCode(String code) {
        for (UserStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return "未知状态";
    }
}