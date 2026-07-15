package com.huangpi.platform.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(40000, HttpStatus.BAD_REQUEST, "参数错误"),
    INVALID_PHONE(40001, HttpStatus.BAD_REQUEST, "手机号格式错误"),
    UNAUTHORIZED(40100, HttpStatus.UNAUTHORIZED, "未登录或登录状态已失效"),
    FORBIDDEN(40300, HttpStatus.FORBIDDEN, "无权限执行该操作"),
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "资源不存在"),
    CONFLICT(40900, HttpStatus.CONFLICT, "当前状态不允许该操作"),
    INTERNAL_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "系统错误");

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
