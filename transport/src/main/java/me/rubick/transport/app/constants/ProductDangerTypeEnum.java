package me.rubick.transport.app.constants;

import me.rubick.common.app.exception.BusinessException;

public enum ProductDangerTypeEnum {

    NO("否"), YES("是");

    private String value;

    ProductDangerTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ProductDangerTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

    public static ProductDangerTypeEnum valOf(String ordinal) throws BusinessException {
        for (ProductDangerTypeEnum e: ProductDangerTypeEnum.values()) {
            if (e.getValue().equals(ordinal)) {
                return e;
            }
        }

        throw new BusinessException("【是否危险品】不是有效值");
    }
}
