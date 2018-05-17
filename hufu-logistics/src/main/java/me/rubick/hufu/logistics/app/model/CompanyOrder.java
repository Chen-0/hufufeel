package me.rubick.hufu.logistics.app.model;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "think_company_order")
public class CompanyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "sender")
    private String sender;

    @Column(name = "sender_phone")
    private String senderPhone;

    @Column(name = "contact")
    private String contact;

    @Column(name = "identity")
    private String identity;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "in_weight")
    private BigDecimal inWeight;

    @Column(name = "declared")
    private BigDecimal declared;

    @Column(name = "out_weight")
    private BigDecimal outWeight;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "comment")
    private String comment;

    @Column(name = "batch")
    private String batch;

    @Column(name = "lg_status")
    private String lgStatus;

    @Column(name = "lg_info")
    private String lgInfo;

    @Column(name = "error_msg")
    private String errorMsg;

    @Column(name = "in_time")
    private Date inTime;

    @Column(name = "out_time")
    private Date outTime;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "company_express_id")
    private Integer companyExpressId;

    @Column(name = "insurance")
    private Integer insurance;

    @ManyToOne
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "company_express_id", insertable = false, updatable = false)
    private CompanyExpress companyExpress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getInWeight() {
        return inWeight;
    }

    public void setInWeight(BigDecimal inWeight) {
        this.inWeight = inWeight;
    }

    public BigDecimal getDeclared() {
        return declared;
    }

    public void setDeclared(BigDecimal declared) {
        this.declared = declared;
    }

    public BigDecimal getOutWeight() {
        return outWeight;
    }

    public void setOutWeight(BigDecimal outWeight) {
        this.outWeight = outWeight;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLgStatus() {
        return lgStatus;
    }

    public void setLgStatus(String lgStatus) {
        this.lgStatus = lgStatus;
    }

    public String getLgInfo() {
        return lgInfo;
    }

    public void setLgInfo(String lgInfo) {
        this.lgInfo = lgInfo;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public int getCompanyExpressId() {
        return companyExpressId;
    }

    public void setCompanyExpressId(Integer companyExpressId) {
        this.companyExpressId = companyExpressId;
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

    public Integer getInsurance() {
        return insurance;
    }

    public void setInsurance(Integer insurance) {
        this.insurance = insurance;
    }
}
