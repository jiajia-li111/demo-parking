package com.tree.plms.enums;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

// 支付方式枚举
public enum PayMethodEnum {
    WECHAT("01", "微信"),
    ALIPAY("02", "支付宝"),
    CASH("03", "现金");

    private final String code;
    private final String desc;

    PayMethodEnum(String code, String desc) {
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
        for (PayMethodEnum method : values()) {
            if (method.getCode().equals(code)) {
                return method.getDesc();
            }
        }
        return null;
    }
}