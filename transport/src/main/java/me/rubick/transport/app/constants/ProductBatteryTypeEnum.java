package me.rubick.transport.app.constants;

public enum ProductBatteryTypeEnum {

    YES("电池类型"), NO("非电池类型");

    private String value;

    ProductBatteryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ProductBatteryTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
