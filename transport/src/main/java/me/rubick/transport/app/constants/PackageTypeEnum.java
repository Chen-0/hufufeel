package me.rubick.transport.app.constants;

public enum PackageTypeEnum {

    NORMAL("入库单"), REJECT("退货单");

    private String value;

    PackageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static PackageTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
