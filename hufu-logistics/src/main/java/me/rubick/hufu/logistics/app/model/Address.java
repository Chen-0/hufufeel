package me.rubick.hufu.logistics.app.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "think_address")
public class Address {

    @Column(name = "zip")
    private Integer zip;

    @Column(name = "identityAmgb")
    private String identityamgb;

    @Column(name = "identityAmga")
    private String identityamga;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "userName")
    private String username;

    @Column(name = "userId")
    private Integer userid;

    @Column(name = "province")
    private String province;

    @Column(name = "addressa")
    private String addressa;

    @Column(name = "identity")
    private String identity;

    @Column(name = "verify")
    private String verify;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "detail")
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getIdentityAmgb() {
        return identityamgb;
    }

    public void setIdentityAmgb(String identityamgb) {
        this.identityamgb = identityamgb;
    }

    public String getIdentityAmga() {
        return identityamga;
    }

    public void setIdentityAmga(String identityamga) {
        this.identityamga = identityamga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        this.userid = userid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddressa() {
        return addressa;
    }

    public void setAddressa(String addressa) {
        this.addressa = addressa;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

