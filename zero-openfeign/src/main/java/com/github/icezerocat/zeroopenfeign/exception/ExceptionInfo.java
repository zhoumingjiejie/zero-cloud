package com.github.icezerocat.zeroopenfeign.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 异常信息
 * CreateDate:  2020/12/8 14:03
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class ExceptionInfo {
    private Long timestamp;

    private Integer status;

    private String exception;

    private String message;

    private String path;

    private String error;
}
