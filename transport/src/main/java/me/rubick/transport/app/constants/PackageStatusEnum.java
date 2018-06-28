package me.rubick.transport.app.constants;

public enum PackageStatusEnum {

    READY("待入库"), RECEIVED("已收货"), FINISH("已上架"), CANCEL("已取消"), NULL("-"), FREEZE("已冻结");

    private String value;

    PackageStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
