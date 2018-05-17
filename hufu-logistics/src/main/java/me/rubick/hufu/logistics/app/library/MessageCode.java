package me.rubick.hufu.logistics.app.library;

public enum MessageCode {
    SUCCESS(0),
    WARN(1),
    ERROR(2);

    private Integer code;

    MessageCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }

    public Integer getCode() {
        return this.code;
    }
}
