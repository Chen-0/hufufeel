package me.rubick.transport.app.constants;

public enum  StatementStatusEnum {

    UNPAY("未支付"), PAY("已支付");

    StatementStatusEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
