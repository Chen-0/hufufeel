package me.rubick.transport.app.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PackageExcelVo implements Serializable {

    private String SKU;

    private int quantity;

    private String contact;

    private String comment;
}
