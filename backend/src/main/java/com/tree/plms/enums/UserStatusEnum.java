package com.tree.plms.enums;

import lombok.Getter;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

@Getter
public enum UserStatusEnum {
    ENABLED("01", "启用"),
    DISABLED("02", "禁用");

    private final String code;
    private final String desc;

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (UserStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return "未知状态";
    }

    // 在UserStatusEnum类中添加
    public static boolean isValidCode(String code) {
        for (UserStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
