package me.rubick.transport.app.model;

public enum OrderStatusEnum {

    CHECK("待审核"), READY("待发货"), SEND("已发货"), FREEZE("已冻结"), FAIL("审核失败"), CANCEL("已取消");

    OrderStatusEnum(String value) {
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
