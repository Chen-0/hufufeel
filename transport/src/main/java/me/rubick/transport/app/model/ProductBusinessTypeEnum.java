package me.rubick.transport.app.model;

public enum ProductBusinessTypeEnum {

    EXPORT("出口业务"), IMPORT("进口业务");
    private String value;

    ProductBusinessTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ProductBusinessTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
