package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Jiazhuo on 2017/3/16.
 */
@Entity
@Table(name = "think_company_express")
public class CompanyExpress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
