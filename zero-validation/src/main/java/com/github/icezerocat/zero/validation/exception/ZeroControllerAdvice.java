package com.github.icezerocat.zero.validation.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: 零控制器建议
 * CreateDate:  2021/7/28 9:46
 *
 * @author zero
 * @version 1.0
 */
@RestControllerAdvice
public class ZeroControllerAdvice {
    @ExceptionHandler(value = BindException.class)
    public String methodArgumentNotValidExceptionHandler(BindException exception) {
        return exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }
}
