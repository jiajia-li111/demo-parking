package com.tree.plms.exception;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */


import com.tree.plms.enums.ResultCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String msg;

    // 基于ResultCodeEnum构造
    public BusinessException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }

    // 自定义消息
    public BusinessException(ResultCodeEnum codeEnum, String customMsg) {
        super(customMsg);
        this.code = codeEnum.getCode();
        this.msg = customMsg;
    }
}