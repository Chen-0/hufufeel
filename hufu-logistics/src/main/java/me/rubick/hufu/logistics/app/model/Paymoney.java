package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "think_paymoney")
public class Paymoney {

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "merchantId")
    private String merchantid;

    @Column(name = "payment_buyer_email")
    private String paymentBuyerEmail;

    @Column(name = "payment_trade_status")
    private String paymentTradeStatus;

    @Column(name = "exter_invoke_ip")
    private String exterInvokeIp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time")
    private Date time;

    @Column(name = "ordstatus")
    private Integer ordstatus;

    @Column(name = "userId")
    private Integer userid;

    @Column(name = "tradeId")
    private String tradeid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getMerchantId() {
        return merchantid;
    }

    public void setMerchantId(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getPayment_buyer_email() {
        return paymentBuyerEmail;
    }

    public void setPayment_buyer_email(String paymentBuyerEmail) {
        this.paymentBuyerEmail = paymentBuyerEmail;
    }

    public String getPayment_trade_status() {
        return paymentTradeStatus;
    }

    public void setPayment_trade_status(String paymentTradeStatus) {
        this.paymentTradeStatus = paymentTradeStatus;
    }

    public String getExter_invoke_ip() {
        return exterInvokeIp;
    }

    public void setExter_invoke_ip(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getOrdstatus() {
        return ordstatus;
    }

    public void setOrdstatus(Integer ordstatus) {
        this.ordstatus = ordstatus;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        this.userid = userid;
    }

    public String getTradeId() {
        return tradeid;
    }

    public void setTradeId(String tradeid) {
        this.tradeid = tradeid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

