package me.rubick.transport.app.vo;

import lombok.Data;
import org.dozer.Mapping;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class ProductFormVo implements Serializable {

    @NotEmpty(message = "商品名称不能为空")
    @Size(min = 0, max = 16, message = "不能超过16个字符")
    private String productName;

    @NotEmpty(message = "商品SKU不能为空")
    @Size(min = 0, max = 12, message = "不能超过12个字符")
    @Pattern(regexp = "^[0-9]{1,12}", message = "请输入数字")
    private String productSku;

    @NotNull(message = "电池类型不能为空")
    private boolean isBattery = false;

    @NotNull(message = "业务类型不能为空")
    private boolean businessType = false;

//    @NotEmpty(message = "原产地不能为空")
    @Size(min = 0, max = 16, message = "不能超过16个字符")
    private String origin = "";

    @NotEmpty(message = "重量不能为空")
    @Digits(integer = 5, fraction = 2, message = "请输入数字（最多两位小数）")
    private String weight;

    @NotEmpty(message = "长不能为空")
    @Digits(integer = 8, fraction = 2, message = "请输入数字（最多两位小数）")
    private String length;

    @NotEmpty(message = "宽不能为空")
    @Digits(integer = 8, fraction = 2, message = "请输入数字（最多两位小数）")
    private String width;

    @NotEmpty(message = "高不能为空")
    @Digits(integer = 8, fraction = 2, message = "请输入数字（最多两位小数）")
    private String height;

    private String productUrl;

//    @NotEmpty(message = "有效时间不能为空")
    @Mapping("this")
    private String deadline;

    @NotNull(message = "是否危险不能为空")
    private boolean isDanger = false;

    @NotEmpty(message = "申报价格不能为空")
    @Digits(integer = 5, fraction = 2, message = "请输入数字（最多两位小数）")
    private String quotedPrice;

    @NotEmpty(message = "申报商品名称不能为空")
    @Size(min = 1, max = 16, message = "不能超过16个字符")
    private String quotedName;

    @Size(min = 0, max = 100, message = "不能超过100个字符")
    private String comment;

    public boolean getIsBattery() {
        return isBattery;
    }

    public void setIsBattery(boolean battery) {
        isBattery = battery;
    }

    public boolean getIsDanger() {
        return isDanger;
    }

    public void setIsDanger(boolean danger) {
        isDanger = danger;
    }
}
