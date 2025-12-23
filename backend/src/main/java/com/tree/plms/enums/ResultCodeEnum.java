package com.tree.plms.enums;

/**
 * 响应状态码枚举（遵循：系统级错误[1-999]，业务级错误[1000+]）
 */
public enum ResultCodeEnum {

    // ========== 系统级成功 ==========
    SUCCESS(200, "操作成功"),

    // ========== 系统级错误（1-999） ==========
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_ERROR(400, "参数格式错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    // ========== 业务级错误（1000+） ==========
    VEHICLE_NOT_FOUND(1001, "未查询到车辆信息"),
    PARKING_FULL(1002, "停车场已满，无空闲车位"),
    SESSION_NOT_FOUND(1003, "未查询到停车会话"),
    GATE_UNAVAILABLE(1004, "通道不可用"),
    PAYMENT_FAILED(1005, "支付失败，请重试"),
    CARD_EXPIRED(1006, "月卡已过期"),
    CARD_INVALID(1007, "月卡状态无效"),
    CARD_EXISTS(1008, "该车辆已存在月卡，请先删除或更新现有月卡"),
    TEMP_VEHICLE_CREATE_FAILED(1009, "创建临时车辆失败，请重试"),
    VEHICLE_ALREADY_PARKING(1010, "该车辆已在停车场内，无法重复入场"),
    VEHICLE_NOT_EXIST(1011, "车辆不存在"),
    VEHICLE_NOT_PARKING(1012, "车辆未在停车场内" ),
     FEE_RULE_NOT_FOUND(1013, "未查询到收费规则"),
    GATE_NOT_FOUND(1014, "未查询到通道信息"),
    VEHICLE_ADD_FAILED(1015, "车辆添加失败，请重试"),
     VEHICLE_UPDATE_FAILED(1016, "车辆更新失败，请重试"),
     VEHICLE_DELETE_FAILED(1017, "车辆删除失败，请重试"),
    OWNER_NOT_FOUND(1018, "未查询到业主信息"),
     OWNER_ADD_FAILED(1019, "业主添加失败，请重试"),
     OWNER_UPDATE_FAILED(1020, "业主更新失败，请重试");

    /**
     * 状态码（建议与HTTP状态码对齐，业务码可自定义）
     */
    private final int code;

    /**
     * 状态描述（用户友好提示）
     */
    private final String msg;

    // 手动添加构造函数
    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 手动添加 getter 方法
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获取枚举
     */
    public static ResultCodeEnum getByCode(int code) {
        for (ResultCodeEnum enumItem : values()) {
            if (enumItem.code == code) {
                return enumItem;
            }
        }
        return SYSTEM_ERROR;
    }
}