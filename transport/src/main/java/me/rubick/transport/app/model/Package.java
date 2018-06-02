package me.rubick.transport.app.model;

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
    private long userId;
    private String nickname;
    private PackageStatus status;
    private long warehouseId;
    private long distributionChannelId;
    private Date createdAt;
    private Date updatedAt;
    private String warehouseName;
    private String distributionChannelName;

    private List<PackageProduct> packageProducts;

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
    @Column(name = "reference_number", nullable = false, length = 32)
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
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
    @Column(name = "distribution_channel_id", nullable = false)
    public long getDistributionChannelId() {
        return distributionChannelId;
    }

    public void setDistributionChannelId(long distributionChannelId) {
        this.distributionChannelId = distributionChannelId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Package that = (Package) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (status != that.status) return false;
        if (warehouseId != that.warehouseId) return false;
        if (distributionChannelId != that.distributionChannelId) return false;
        if (referenceNumber != null ? !referenceNumber.equals(that.referenceNumber) : that.referenceNumber != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (referenceNumber != null ? referenceNumber.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + status.ordinal();
        result = 31 * result + (int) (warehouseId ^ (warehouseId >>> 32));
        result = 31 * result + (int) (distributionChannelId ^ (distributionChannelId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Column(name = "warehouse_name")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Column(name = "distribution_channel_name")
    public String getDistributionChannelName() {
        return distributionChannelName;
    }

    public void setDistributionChannelName(String distributionChannelName) {
        this.distributionChannelName = distributionChannelName;
    }

    @OneToMany
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    public List<PackageProduct> getPackageProducts() {
        return packageProducts;
    }

    public void setPackageProducts(List<PackageProduct> packageProducts) {
        this.packageProducts = packageProducts;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}