package me.rubick.transport.app.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CostSubjectSnapshotVo implements Serializable {

    private String rkt;
    private BigDecimal rkv;
    private String sjt;
    private BigDecimal sjv;
    private String ddt;
    private List<BigDecimal> ddv;
    private String thrkt;
    private BigDecimal thrkv;
    private String thsjt;
    private BigDecimal thsjv;
}
