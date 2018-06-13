package me.rubick.transport.app.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 费用组成
 */
@Data
public class CostSnapshotVo implements Serializable {

    private BigDecimal CZF = BigDecimal.ZERO;

    private BigDecimal ZYF = BigDecimal.ZERO;
}
