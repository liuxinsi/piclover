package com.lxs.gollum.precious;

/**
 * @author liuxinsi
 * @date 2018/9/11 15:12
 */
public class PreciousException extends RuntimeException{
    public PreciousException() {
    }

    public PreciousException(String message) {
        super(message);
    }

    public PreciousException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreciousException(Throwable cause) {
        super(cause);
    }

    public PreciousException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
