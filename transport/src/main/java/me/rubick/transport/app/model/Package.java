package me.rubick.transport.app.model;

import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.PackageTypeEnum;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rubick_package")
public class Package {
    private long id;
    private String referenceNumber;
    private String contact;
    private long userId;
    private String nickname;
    private PackageStatusEnum status;
    private long warehouseId;
    private Date createdAt;
    private Date updatedAt;
    private String warehouseName;
    private int quantity;
    private String comment;
    private String sn;
    private int expectQuantity;
    private PackageStatusEnum nextStatus = PackageStatusEnum.NULL;
    private String cn;
    private PackageTypeEnum type;
    private String searchNo = "";
    private boolean isDelete = false;

    private List<PackageProduct> packageProducts;
    private User user;

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
    @Column(name = "status", nullable = false)
    public PackageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PackageStatusEnum status) {
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

    @Column(name = "warehouse_name")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @OneToMany
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    public List<PackageProduct> getPackageProducts() {
        return packageProducts;
    }

    public void setPackageProducts(List<PackageProduct> packageProducts) {
        this.packageProducts = packageProducts;
    }

    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Column
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "expect_quantity")
    public int getExpectQuantity() {
        return expectQuantity;
    }

    public void setExpectQuantity(int expectQuantity) {
        this.expectQuantity = expectQuantity;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "next_status")
    public PackageStatusEnum getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(PackageStatusEnum nextStatus) {
        this.nextStatus = nextStatus;
    }

    @Column(name = "cn")
    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    @Column
    public PackageTypeEnum getType() {
        return type;
    }

    public void setType(PackageTypeEnum type) {
        this.type = type;
    }

    @Column(name = "contact", nullable = false, length = 32)
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column(name = "reference_number", nullable = false, length = 32)
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Column(name = "search_no")
    public String getSearchNo() {
        return searchNo;
    }

    public void setSearchNo(String searchNo) {
        this.searchNo = searchNo;
    }

    @Column(name = "is_delete")
    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean delete) {
        isDelete = delete;
    }
}
