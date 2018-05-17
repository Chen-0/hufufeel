package me.rubick.hufu.logistics.app.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "`time`")
    private Integer time;

    @Column(name = "`in`")
    private BigDecimal in;

    @Column(name = "`out`")
    private BigDecimal out;

    @Column(name = "total_waybill")
    private Integer totalWaybill;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTotalWaybill() {
        return totalWaybill;
    }

    public void setTotalWaybill(Integer totalWaybill) {
        this.totalWaybill = totalWaybill;
    }

    public BigDecimal getOut() {
        return out;
    }

    public void setOut(BigDecimal out) {
        this.out = out;
    }

    public BigDecimal getIn() {
        return in;
    }

    public void setIn(BigDecimal in) {
        this.in = in;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
