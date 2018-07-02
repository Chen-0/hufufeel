package me.rubick.transport.app.vo;

import lombok.Data;

@Data
public class SwitchSkuFormVo extends AbstractVo {
    private String sku;
    private long uploadfileId;
    private String uploadfileName;
}
