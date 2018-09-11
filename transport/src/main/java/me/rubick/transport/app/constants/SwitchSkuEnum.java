package me.rubick.transport.app.constants;

public enum SwitchSkuEnum {

    SUBMIT("提交换标"), CHANGING("正在换标"), FINISH("换标成功");

    SwitchSkuEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static SwitchSkuEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
