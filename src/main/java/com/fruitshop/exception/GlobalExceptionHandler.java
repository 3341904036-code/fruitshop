package com.fruitshop. exception;

import com.fruitshop.util.ResponseUtil;
import lombok.extern.slf4j. Slf4j;
import org. springframework.web.bind.MethodArgumentNotValidException;
import org.springframework. web.bind.annotation.ExceptionHandler;
import org.springframework. web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException. class)
    public ResponseUtil<? > handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: {}", e. getMessage());
        return ResponseUtil.error(400, e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseUtil<?> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("参数验证异常: {}", message);
        return ResponseUtil.error(400, message);
    }

    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseUtil<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常", e);
        return ResponseUtil. error(500, "系统异常，请稍后重试");
    }
}