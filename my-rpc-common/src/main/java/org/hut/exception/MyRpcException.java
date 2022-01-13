package org.hut.exception;

public class MyRpcException extends RuntimeException {

    public MyRpcException(String message) {
        super(message);
    }

    public MyRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
