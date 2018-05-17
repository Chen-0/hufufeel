package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "think_company_r_express")
public class CompanyRExpress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "express_id")
    private Integer expressId;

    @Column
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "company_id", updatable = false, insertable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "express_id", updatable = false, insertable = false)
    private CompanyExpress companyExpress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyExpress getCompanyExpress() {
        return companyExpress;
    }

    public void setCompanyExpress(CompanyExpress companyExpress) {
        this.companyExpress = companyExpress;
    }
}
