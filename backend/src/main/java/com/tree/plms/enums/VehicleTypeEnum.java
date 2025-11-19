package com.tree.plms.enums;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// 车辆类型枚举
public enum VehicleTypeEnum {
    SMALL("01", "小型车"),
    LARGE("02", "大型车");

    private final String code;
    private final String desc;

    VehicleTypeEnum(String code, String desc) {
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
        for (VehicleTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }
}
