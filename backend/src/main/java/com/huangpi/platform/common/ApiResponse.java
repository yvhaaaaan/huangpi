package com.huangpi.platform.common;

public record ApiResponse<T>(int code, String message, T data, String traceId) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data, TraceContext.currentTraceId());
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> error(int code, String message) {
        return new ApiResponse<>(code, message, null, TraceContext.currentTraceId());
    }
}
