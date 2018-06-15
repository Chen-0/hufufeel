package me.rubick.transport.app.model;

import me.rubick.transport.app.constants.StatementStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rubick_statements")
public class Statements {
    private long id;
    private long userId;
    private StatementTypeEnum type;
    private BigDecimal total;
    private String target;
    private String comment;
    private StatementStatusEnum status;
    private Date createdAt;
    private Date payAt;

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
    @Column(name = "type", nullable = false, length = 16)
    public StatementTypeEnum getType() {
        return type;
    }

    public void setType(StatementTypeEnum type) {
        this.type = type;
    }

    @Basic
    @Column(name = "total", nullable = false, precision = 2)
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
    @Column(name = "target", nullable = false, length = 32)
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Basic
    @Column(name = "comment", nullable = false, length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public StatementStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatementStatusEnum status) {
        this.status = status;
    }

    @Column(name = "pay_at")
    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }
}
