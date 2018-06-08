package me.rubick.transport.app.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductWarehouseVo implements Serializable {

    private long id;
    private long warehouseId;
    private long productId;
    private long userId;
    private int quantity;
    private BigDecimal weight;
    private String productName;
    private String productSku;
}
