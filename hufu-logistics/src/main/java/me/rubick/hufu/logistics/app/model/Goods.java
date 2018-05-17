package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;

@Entity
@Table(name = "think_goods")
public class Goods {

    @Column(name = "goodname")
    private String goodname;

    @Column(name = "contnum")
    private String contnum;

    @Column(name = "goodb")
    private String goodb;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "gooda")
    private String gooda;

    @Column(name = "farherId")
    private Integer farherid;

    @Column(name = "brandname")
    private String brandname;

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getContnum() {
        return contnum;
    }

    public void setContnum(String contnum) {
        this.contnum = contnum;
    }

    public String getGoodb() {
        return goodb;
    }

    public void setGoodb(String goodb) {
        this.goodb = goodb;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGooda() {
        return gooda;
    }

    public void setGooda(String gooda) {
        this.gooda = gooda;
    }

    public Integer getFarherId() {
        return farherid;
    }

    public void setFarherId(Integer farherid) {
        this.farherid = farherid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

}

