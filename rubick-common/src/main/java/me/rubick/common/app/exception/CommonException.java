package me.rubick.common.app.exception;

public class CommonException extends Exception {

    public CommonException(Exception e) {
        super(e);
    }

    public CommonException(String msg) {
        super(msg);
    }

    public CommonException() {
        super();
    }
}
