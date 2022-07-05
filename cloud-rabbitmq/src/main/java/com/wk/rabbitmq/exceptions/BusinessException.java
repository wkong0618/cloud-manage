package com.wk.rabbitmq.exceptions;


/**
 * @Description : 业务异常
 * @Author : wukong
 */
public class BusinessException extends RuntimeException{
    protected int code;
    protected String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, Throwable e) {
        super(e);
        this.code = code;
        this.message = message;
    }
}
