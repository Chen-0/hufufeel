package me.rubick.transport.app.vo;

import lombok.Data;
import org.dozer.Mapping;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductFormVo extends AbstractVo {


    private String productName;
    private String productSku;
    private int isBattery = 0;
    private int businessType = 0;
    private String origin = "";
    private String weight;
    private String length;
    private String width;
    private String height;
    private String productUrl;
    @Mapping("this")
    private String deadline = "";
    private int isDanger = 0;
    private String quotedPrice;
    private String quotedName;
    private String comment;
    private Long imageId;
    private String imageName;
    private String type;

    public int getIsBattery() {
        return isBattery;
    }

    public void setIsBattery(int battery) {
        isBattery = battery;
    }

    public int getIsDanger() {
        return isDanger;
    }

    public void setIsDanger(int danger) {
        isDanger = danger;
    }
}
