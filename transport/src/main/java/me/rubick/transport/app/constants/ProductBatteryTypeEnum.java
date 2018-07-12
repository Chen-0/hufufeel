package me.rubick.transport.app.constants;

import me.rubick.common.app.exception.BusinessException;

public enum ProductBatteryTypeEnum {

    NO("非电池类型"), YES("电池类型");

    private String value;

    ProductBatteryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ProductBatteryTypeEnum valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

    public static ProductBatteryTypeEnum valOf(String ordinal) throws BusinessException {
        for (ProductBatteryTypeEnum e: ProductBatteryTypeEnum.values()) {
            if (e.getValue().equals(ordinal)) {
                return e;
            }
        }

        throw new BusinessException("【电池类型】不是有效值");
    }
}
