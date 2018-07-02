package me.rubick.transport.app.model;

import me.rubick.transport.app.constants.PaymentStatusEnum;
import me.rubick.transport.app.constants.PaymentTypeEnum;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rubick_payment")
public class Payment {
    private long id;
    private long userId;
    private String outTradeNo;
    private String extendsInfo;
    private PaymentTypeEnum type;
    private PaymentStatusEnum status;
    private BigDecimal totalFee;
    private Date createdAt;
    private Date successAt;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "out_trade_no", nullable = false, length = 64)
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Basic
    @Column(name = "extends_info", nullable = false, length = -1)
    public String getExtendsInfo() {
        return extendsInfo;
    }

    public void setExtendsInfo(String extendsInfo) {
        this.extendsInfo = extendsInfo;
    }

    @Basic
    @Column(name = "type")
    public PaymentTypeEnum getType() {
        return type;
    }

    public void setType(PaymentTypeEnum type) {
        this.type = type;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public PaymentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusEnum status) {
        this.status = status;
    }

    @Basic
    @Column(name = "total_fee", nullable = false, precision = 2)
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    @Basic
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.INSERT)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "success_at", nullable = true)
    public Date getSuccessAt() {
        return successAt;
    }

    public void setSuccessAt(Date successAt) {
        this.successAt = successAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment that = (Payment) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (status != that.status) return false;
        if (outTradeNo != null ? !outTradeNo.equals(that.outTradeNo) : that.outTradeNo != null) return false;
        if (extendsInfo != null ? !extendsInfo.equals(that.extendsInfo) : that.extendsInfo != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (totalFee != null ? !totalFee.equals(that.totalFee) : that.totalFee != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (successAt != null ? !successAt.equals(that.successAt) : that.successAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (outTradeNo != null ? outTradeNo.hashCode() : 0);
        result = 31 * result + (extendsInfo != null ? extendsInfo.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (successAt != null ? successAt.hashCode() : 0);
        return result;
    }
}
