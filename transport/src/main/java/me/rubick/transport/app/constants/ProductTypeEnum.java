package me.rubick.transport.app.constants;

public enum ProductTypeEnum {

    NORMAL("正常商品"), REJECT("退货商品");

    private String value;

    ProductTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
