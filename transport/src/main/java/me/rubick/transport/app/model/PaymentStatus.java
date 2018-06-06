package me.rubick.transport.app.model;

public enum PaymentStatus {
    FALSE("未支付"),
    TRUE("已支付");

    private String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
