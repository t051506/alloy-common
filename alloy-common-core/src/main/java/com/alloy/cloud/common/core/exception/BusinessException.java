package com.alloy.cloud.common.core.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 2959461178304809693L;

    private String appCode;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
