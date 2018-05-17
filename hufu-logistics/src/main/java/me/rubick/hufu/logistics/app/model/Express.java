package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;

@Entity
@Table(name = "think_express")
public class Express {

    @Column(name = "insurance")
    private Integer insurance;

    @Column(name = "limit")
    private Integer limit;

    @Column(name = "firstcost")
    private Integer firstcost;

    @Column(name = "express")
    private String express;

    @Column(name = "beizhu")
    private String beizhu;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nextcost")
    private Integer nextcost;

    public Integer getInsurance() {
        return insurance;
    }

    public void setInsurance(Integer insurance) {
        this.insurance = insurance;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getFirstcost() {
        return firstcost;
    }

    public void setFirstcost(Integer firstcost) {
        this.firstcost = firstcost;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNextcost() {
        return nextcost;
    }

    public void setNextcost(Integer nextcost) {
        this.nextcost = nextcost;
    }

}

