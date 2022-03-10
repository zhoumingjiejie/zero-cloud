package com.github.icezerocat.zero.jdbc.builder;

/**
 * @author dragons
 * @date 2021/11/10 13:04
 */
public class SQLBuilderException extends RuntimeException {
    public SQLBuilderException() {
    }

    public SQLBuilderException(String message) {
        super(message);
    }

    public SQLBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLBuilderException(Throwable cause) {
        super(cause);
    }

    public SQLBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
