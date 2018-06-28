package me.rubick.transport.app.vo.admin;

import lombok.Data;
import me.rubick.transport.app.vo.AbstractVo;

@Data
public class OrderFormVo extends AbstractVo {

    private String express;
    private String expressNo;
}
