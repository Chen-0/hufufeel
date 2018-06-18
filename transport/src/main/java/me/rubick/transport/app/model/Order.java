package me.rubick.transport.app.model;

import me.rubick.transport.app.vo.CostSnapshotVo;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rubick_order")
public class Order implements Serializable {
    private long id;
    private long userId;
    private String sn;
    private String referenceNumber;
    private String tn;
    private OrderStatusEnum status;
    private long warehouseId;
    private String warehouseName;
    private BigDecimal total;
    private BigDecimal weight;
    private Date createdAt;
    private Date updatedAt;
    private String orderSnapshot;
    private String comment;
    private String reason;
    private String phone;
    private String contact;
    private int quantity;
    private int skuQty;
    private String costSnapshot;
    private Date outTime;
    private String express = "";
    private String expressNo = "";
    private OrderStatusEnum nextStatus = OrderStatusEnum.NULL;

    @Transient
    private OrderSnapshotVo orderSnapshotVo;

    @Transient
    private CostSnapshotVo costSnapshotVo;


    private List<OrderItem> orderItems;

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
    @Column(name = "sn", nullable = false, length = 32)
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Basic
    @Column(name = "reference_number", nullable = false, length = 32)
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Basic
    @Column(name = "tn", nullable = false, length = 32)
    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    @Basic
    @Column(name = "warehouse_id", nullable = false)
    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "warehouse_name", nullable = false, length = 32)
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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
    @Column(name = "weight", nullable = false, precision = 2)
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Basic
    @Column(name = "order_snapshot", nullable = false, length = -1)
    public String getOrderSnapshot() {
        return orderSnapshot;
    }

    public void setOrderSnapshot(String orderSnapshot) {
        this.orderSnapshot = orderSnapshot;
    }

    @Transient
    public OrderSnapshotVo getOrderSnapshotVo() {
        return orderSnapshotVo;
    }

    public void setOrderSnapshotVo(OrderSnapshotVo orderSnapshotVo) {
        this.orderSnapshotVo = orderSnapshotVo;
    }

    @Column
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "sku_qty")
    public int getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(int skuQty) {
        this.skuQty = skuQty;
    }

    @Column(name = "cost_snapshot")
    public String getCostSnapshot() {
        return costSnapshot;
    }

    public void setCostSnapshot(String costSnapshot) {
        this.costSnapshot = costSnapshot;
    }

    @Transient
    public CostSnapshotVo getCostSnapshotVo() {
        return costSnapshotVo;
    }

    public void setCostSnapshotVo(CostSnapshotVo costSnapshotVo) {
        this.costSnapshotVo = costSnapshotVo;
    }

    @Column(name = "out_time")
    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    @OneToMany
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Column
    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    @Column(name = "express_no")
    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    @Column(name = "next_status")
    public OrderStatusEnum getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(OrderStatusEnum nextStatus) {
        this.nextStatus = nextStatus;
    }
}
