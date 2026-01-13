package com.fruitshop.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 统一响应工具类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUtil<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    /**
     * 成功响应
     */
    public static <T> ResponseUtil<T> success(String message, T data) {
        return ResponseUtil.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 成功响应 - 仅有数据
     */
    public static <T> ResponseUtil<T> success(T data) {
        return ResponseUtil.<T>builder()
                .code(200)
                .message("操作成功")
                .data(data)
                .build();
    }

    /**
     * 成功响应 - 仅有消息
     */
    public static ResponseUtil<? > success(String message) {
        return ResponseUtil. builder()
                .code(200)
                .message(message)
                .build();
    }

    /**
     * 失败响应
     */
    public static <T> ResponseUtil<T> error(Integer code, String message) {
        return ResponseUtil.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * 失败响应 - 默认500
     */
    public static <T> ResponseUtil<T> error(String message) {
        return ResponseUtil. <T>builder()
                .code(500)
                .message(message)
                .build();
    }
}