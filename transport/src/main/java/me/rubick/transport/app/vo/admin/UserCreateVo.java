package me.rubick.transport.app.vo.admin;

import lombok.Data;
import me.rubick.transport.app.vo.AbstractVo;

@Data
public class UserCreateVo extends AbstractVo {
    private String username;
    private String password;
    private String name;
    private String csPhone;
    private String csQQ;
    private String hwcSn;
}
