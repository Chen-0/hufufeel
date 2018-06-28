package me.rubick.transport.app.constants;

public enum PaymentStatusEnum {
    FALSE("未支付"),
    TRUE("已支付");

    private String value;

    PaymentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
