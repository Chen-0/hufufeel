package me.rubick.transport.app.vo;

import lombok.Data;
import me.rubick.transport.app.constants.ProductStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductSnapshotVo implements Serializable {
    private long id;
    private long userId;
    private String productName;
    private String productSku;
    private boolean isBattery;
    private String origin;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private ProductStatusEnum status;
    private long imageId;
    private Date deadline;
    private boolean isDanger;
    private BigDecimal quotedPrice;
    private String quotedName;
    private String comment;
    private String reason;
    private boolean isDeleted;
    private Date createdAt;
    private Date updatedAt;
    private boolean businessType;
}
