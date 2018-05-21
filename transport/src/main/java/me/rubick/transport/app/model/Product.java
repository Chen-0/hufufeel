package me.rubick.transport.app.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rubick_product")
public class Product {
    private long id;
    private long userId;
    private String productName;
    private String productSku;
    private boolean isBattery;
    private String origin;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private ProductStatus status;
    private String productUrl;
    private Date deadline;
    private boolean isDanger;
    private BigDecimal quotedPrice;
    private String quotedName;
    private String comment;
    private Date createdAt;
    private Date updatedAt;

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
    @Column(name = "product_name", nullable = false, length = 255)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "product_sku", nullable = false, length = 255)
    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    @Basic
    @Column(name = "is_battery", nullable = false)
    public boolean getIsBattery() {
        return isBattery;
    }

    public void setIsBattery(boolean isBattery) {
        this.isBattery = isBattery;
    }

    @Basic
    @Column(name = "origin", nullable = false, length = 255)
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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
    @Column(name = "length", nullable = false, precision = 2)
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    @Basic
    @Column(name = "width", nullable = false, precision = 2)
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    @Basic
    @Column(name = "height")
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Basic
    @Column(name = "product_url", nullable = false, length = 255)
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    @Basic
    @Column(name = "deadline", nullable = true)
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Basic
    @Column(name = "is_danger", nullable = false)
    public boolean getIsDanger() {
        return isDanger;
    }

    public void setIsDanger(boolean isDanger) {
        this.isDanger = isDanger;
    }

    @Basic
    @Column(name = "quoted_price", nullable = false, precision = 2)
    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(BigDecimal quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    @Basic
    @Column(name = "quoted_name", nullable = false, length = 255)
    public String getQuotedName() {
        return quotedName;
    }

    public void setQuotedName(String quotedName) {
        this.quotedName = quotedName;
    }

    @Basic
    @Column(name = "comment", length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

        Product that = (Product) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (isBattery != that.isBattery) return false;
        if (status != that.status) return false;
        if (isDanger != that.isDanger) return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        if (productSku != null ? !productSku.equals(that.productSku) : that.productSku != null) return false;
        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (productUrl != null ? !productUrl.equals(that.productUrl) : that.productUrl != null) return false;
        if (deadline != null ? !deadline.equals(that.deadline) : that.deadline != null) return false;
        if (quotedPrice != null ? !quotedPrice.equals(that.quotedPrice) : that.quotedPrice != null) return false;
        if (quotedName != null ? !quotedName.equals(that.quotedName) : that.quotedName != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productSku != null ? productSku.hashCode() : 0);
        result = 31 * result + (isBattery ? 1 : 0);
        result = 31 * result + (origin != null ? origin.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + status.ordinal();
        result = 31 * result + (productUrl != null ? productUrl.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (isDanger ? 1 : 0);
        result = 31 * result + (quotedPrice != null ? quotedPrice.hashCode() : 0);
        result = 31 * result + (quotedName != null ? quotedName.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
