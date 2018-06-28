package me.rubick.common.app.exception;

public class NotFoundException extends Exception {

    public NotFoundException(Exception e) {
        super(e);
    }

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException() {
        super();
    }
}
