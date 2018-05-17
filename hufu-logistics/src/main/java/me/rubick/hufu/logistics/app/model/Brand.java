package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "think_brand")
public class Brand {

    @Column(name = "fatherId")
    private Integer fatherid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "brand")
    private String brand;

    @OneToMany
    @JoinColumn(name = "fatherId", insertable = false, updatable = false)
    private List<Brand> children;

    public Integer getFatherId() {
        return fatherid;
    }

    public void setFatherId(Integer fatherid) {
        this.fatherid = fatherid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<Brand> getChildren() {
        return children;
    }

    public void setChildren(List<Brand> children) {
        this.children = children;
    }
}

