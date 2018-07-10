package me.rubick.transport.app.vo.admin;

import lombok.Data;
import me.rubick.transport.app.vo.AbstractVo;

@Data
public class NoticeFormVo extends AbstractVo {

    private String title;
    private String context;
}
