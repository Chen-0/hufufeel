package me.rubick.transport.app.constants;

import me.rubick.common.app.exception.BusinessException;

public enum ProductBusinessTypeEnum {

    EXPORT("出口业务"), IMPORT("进口业务");
    private String value;

    ProductBusinessTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ProductBusinessTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

    public static ProductBusinessTypeEnum valOf(String ordinal) throws BusinessException {
        for (ProductBusinessTypeEnum e: ProductBusinessTypeEnum.values()) {
            if (e.getValue().equals(ordinal)) {
                return e;
            }
        }

        throw new BusinessException("【业务类型的值】不是有效值");
    }
}
