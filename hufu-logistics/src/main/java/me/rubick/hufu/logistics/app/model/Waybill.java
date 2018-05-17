package me.rubick.hufu.logistics.app.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "think_waybill")
public class Waybill {

    @Column(name = "TrackingNumber")
    private String trackingnumber;

    @Column(name = "express")
    private String express;

    @Column(name = "isOurGoodId")
    private Integer isourgoodid;

    @Column(name = "isOurGood")
    private String isourgood;

    @Column(name = "logistics_status")
    private String logisticsStatus;

    @Column(name = "arrive")
    private Integer arrive;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "old_tracking_number")
    private String oldTrackingNumber;

    @Column(name = "allCost")
    private BigDecimal allcost;

    @Column(name = "extra_cost")
    private BigDecimal extraCost;

    @Column(name = "outExpress")
    private String outexpress;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "paymentStatus")
    private String paymentstatus;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "address")
    private String address;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "extra_cost_reason")
    private String extraCostReason;

    @Column(name = "expressnum")
    private String expressnum;

    @Column(name = "userId")
    private Integer userid;

    @Column(name = "declareCost")
    private BigDecimal declarecost;

    @Column(name = "agea")
    private String agea;

    @Column(name = "inTime")
    private Date intime;

    @Column(name = "insuranceCost")
    private BigDecimal insurancecost;

    @Column(name = "mendCost")
    private BigDecimal mendcost;

    @Column(name = "expressName")
    private String expressname;

    @Column(name = "createTime")
    private Date createtime;

    @Column(name = "storageNo")
    private String storageno;

    @Column(name = "outWeight")
    private BigDecimal outweight;

    @Column(name = "mend_reason")
    private String mendReason;

    @Column(name = "beizhu")
    private String beizhu;

    @Column(name = "operation")
    private String operation;

    @Column(name = "outTime")
    private Date outtime;

    @Column(name = "username")
    private String username;

    @Column(name = "expressId")
    private Integer expressid;

    @Column(name = "customer")
    private String customer;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address", updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Address userAddress = null;

    @OneToMany
    @JoinColumn(name = "farherId", updatable = false, insertable = false)
    private List<Goods> goods;

    public String getTrackingNumber() {
        return trackingnumber;
    }

    public void setTrackingNumber(String trackingnumber) {
        this.trackingnumber = trackingnumber;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public Integer getIsOurGoodId() {
        return isourgoodid;
    }

    public void setIsOurGoodId(Integer isourgoodid) {
        this.isourgoodid = isourgoodid;
    }

    public String getIsOurGood() {
        return isourgood;
    }

    public void setIsOurGood(String isourgood) {
        this.isourgood = isourgood;
    }

    public String getLogistics_status() {
        return logisticsStatus;
    }

    public void setLogistics_status(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public Integer getArrive() {
        return arrive;
    }

    public void setArrive(Integer arrive) {
        this.arrive = arrive;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOld_tracking_number() {
        return oldTrackingNumber;
    }

    public void setOld_tracking_number(String oldTrackingNumber) {
        this.oldTrackingNumber = oldTrackingNumber;
    }

    public BigDecimal getAllCost() {
        return allcost;
    }

    public void setAllCost(BigDecimal allcost) {
        this.allcost = allcost;
    }

    public BigDecimal getExtra_cost() {
        return extraCost;
    }

    public void setExtra_cost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public String getOutExpress() {
        return outexpress;
    }

    public void setOutExpress(String outexpress) {
        this.outexpress = outexpress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentStatus() {
        return paymentstatus;
    }

    public void setPaymentStatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getExtra_cost_reason() {
        return extraCostReason;
    }

    public void setExtra_cost_reason(String extraCostReason) {
        this.extraCostReason = extraCostReason;
    }

    public String getExpressnum() {
        return expressnum;
    }

    public void setExpressnum(String expressnum) {
        this.expressnum = expressnum;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getDeclareCost() {
        return declarecost;
    }

    public void setDeclareCost(BigDecimal declarecost) {
        this.declarecost = declarecost;
    }

    public String getAgea() {
        return agea;
    }

    public void setAgea(String agea) {
        this.agea = agea;
    }

    public Date getInTime() {
        return intime;
    }

    public void setInTime(Date intime) {
        this.intime = intime;
    }

    public BigDecimal getInsuranceCost() {
        return insurancecost;
    }

    public void setInsuranceCost(BigDecimal insurancecost) {
        this.insurancecost = insurancecost;
    }

    public BigDecimal getMendCost() {
        return mendcost;
    }

    public void setMendCost(BigDecimal mendcost) {
        this.mendcost = mendcost;
    }

    public String getExpressName() {
        return expressname;
    }

    public void setExpressName(String expressname) {
        this.expressname = expressname;
    }

    public Date getCreateTime() {
        return createtime;
    }

    public void setCreateTime(Date createtime) {
        this.createtime = createtime;
    }

    public String getStorageNo() {
        return storageno;
    }

    public void setStorageNo(String storageno) {
        this.storageno = storageno;
    }

    public BigDecimal getOutWeight() {
        return outweight;
    }

    public void setOutWeight(BigDecimal outweight) {
        this.outweight = outweight;
    }

    public String getMend_reason() {
        return mendReason;
    }

    public void setMend_reason(String mendReason) {
        this.mendReason = mendReason;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOutTime() {
        return outtime;
    }

    public void setOutTime(Date outtime) {
        this.outtime = outtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getExpressId() {
        return expressid;
    }

    public void setExpressId(Integer expressid) {
        this.expressid = expressid;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address userAddress) {
        this.userAddress = userAddress;
    }
}

