package com.tree.plms.model.dto.response;

/**
 * @author SuohaChan
 * @data 2025/11/12
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree.plms.enums.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 * @param <T> 响应数据类型
 */
@Data
@ApiModel(description = "统一响应结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码（200=成功，其他=失败）", required = true, example = "200")
    private int code;

    @ApiModelProperty(value = "响应消息（成功/失败描述）", required = true, example = "操作成功")
    private String msg;

    @ApiModelProperty(value = "响应数据（成功时返回，失败时为null）")
    @JsonInclude(JsonInclude.Include.NON_NULL) // 数据为null时不序列化
    private T data;

    // ========== 私有构造器（禁止直接new，通过静态方法构建） ==========
    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ========== 成功响应（无数据/有数据） ==========
    public static <T> Result<T> success() {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    // ========== 失败响应（基于枚举/自定义消息） ==========
    public static <T> Result<T> fail(ResultCodeEnum codeEnum) {
        return new Result<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> Result<T> fail(ResultCodeEnum codeEnum, String customMsg) {
        return new Result<>(codeEnum.getCode(), customMsg, null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // ========== 快速判断是否成功 ==========
    public boolean isSuccess() {
        return this.code == ResultCodeEnum.SUCCESS.getCode();
    }
}