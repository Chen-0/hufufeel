package me.rubick.transport.app.model;

public enum ProductStatus {

    TO_CHECK("待审核"), READY_CHECK("审核成功"), FAIL_CHECK("审核失败");

    private String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
