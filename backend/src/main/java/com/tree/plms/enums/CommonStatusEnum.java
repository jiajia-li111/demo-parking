package com.tree.plms.enums;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// 公共状态枚举（通用01=启用/是，02=禁用/否）
public enum CommonStatusEnum {
    ENABLE("01", "启用/是"),
    DISABLE("02", "禁用/否");

    private final String code;
    private final String desc;

    CommonStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (CommonStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}


