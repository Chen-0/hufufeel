package me.rubick.transport.app.constants;

public enum StatementTypeEnum {

    RK("入库费用"), SJ("上架费用"), ORDER("订单费用"), STORE("仓租费");

    StatementTypeEnum(String value) {
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
