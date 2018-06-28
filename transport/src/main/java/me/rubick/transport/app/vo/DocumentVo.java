package me.rubick.transport.app.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class DocumentVo implements Serializable {
    private long id;
    private String name;
    private String originalFilename;
}
