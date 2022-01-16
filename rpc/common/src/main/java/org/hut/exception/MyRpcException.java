package org.hut.exception;

public class MyRpcException extends RuntimeException{

    public MyRpcException() {
    }

    public MyRpcException(String message) {
        super(message);
    }

    public MyRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyRpcException(Throwable cause) {
        super(cause);
    }
}
