package me.rubick.transport.app.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "rubick_payment", schema = "hufufeel", catalog = "")
public class Payment {
    private long id;
    private long userId;
    private String outTradeNo;
    private String extendsInfo;
    private String type;
    private int status;
    private BigDecimal totalFee;
    private Timestamp createdAt;
    private Timestamp successAt;

    @Id
    @Column(name = "id", nullable = false)
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
    @Column(name = "type", nullable = false, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "success_at", nullable = true)
    public Timestamp getSuccessAt() {
        return successAt;
    }

    public void setSuccessAt(Timestamp successAt) {
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
        result = 31 * result + status;
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (successAt != null ? successAt.hashCode() : 0);
        return result;
    }
}
