package me.rubick.transport.app.vo.admin;

import lombok.Data;
import me.rubick.transport.app.vo.AbstractVo;

@Data
public class OrderCheckOutVo extends AbstractVo {
    private String total;
    private String express;
    private String expressNo;
}
