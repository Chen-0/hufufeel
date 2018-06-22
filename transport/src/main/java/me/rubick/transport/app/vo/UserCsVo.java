package me.rubick.transport.app.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCsVo implements Serializable {
    private String csPhone;
    private String csQQ;
}
