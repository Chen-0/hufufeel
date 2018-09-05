package me.rubick.transport.app.vo.admin;

import lombok.Data;
import me.rubick.transport.app.vo.AbstractVo;

import java.math.BigDecimal;

@Data
public class RechargeFormVo extends AbstractVo {

    private BigDecimal total;
    private String comment;
}
