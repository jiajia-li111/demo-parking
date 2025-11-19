package com.tree.plms.enums;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// 车位状态枚举
public enum SpaceStatusEnum {
    FREE("01", "空闲"),
    OCCUPIED("02", "占用"),
    FAULT("03", "故障");

    private final String code;
    private final String desc;

    SpaceStatusEnum(String code, String desc) {
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
        for (SpaceStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}