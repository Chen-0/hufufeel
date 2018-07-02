package me.rubick.transport.app.constants;

public enum PaymentTypeEnum {

    ALIPAY("支付宝"), SYSTEM("系统");

    private String value;

    PaymentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
