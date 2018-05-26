package me.rubick.transport.app.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rubick_warehouse")
public class Warehouse {
    private long id;
    private String name;
    private String addressOne;
    private String addressTwo;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private List<DistributionChannel> distributionChannels;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "address_one", nullable = false, length = 64)
    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    @Basic
    @Column(name = "address_two", nullable = false, length = 64)
    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    @Basic
    @Column(name = "city", nullable = false, length = 64)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "state", nullable = false, length = 64)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "zip", nullable = false, length = 64)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Basic
    @Column(name = "phone", nullable = false, length = 64)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse that = (Warehouse) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (addressOne != null ? !addressOne.equals(that.addressOne) : that.addressOne != null) return false;
        if (addressTwo != null ? !addressTwo.equals(that.addressTwo) : that.addressTwo != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (zip != null ? !zip.equals(that.zip) : that.zip != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (addressOne != null ? addressOne.hashCode() : 0);
        result = 31 * result + (addressTwo != null ? addressTwo.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @ManyToMany
    @JoinTable(
            name = "rubick_distribution_channel_warehoure",
            joinColumns = @JoinColumn(name = "warehouse_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "distribution_channel_id", insertable = false, updatable = false)
    )
    public List<DistributionChannel> getDistributionChannels() {
        return distributionChannels;
    }

    public void setDistributionChannels(List<DistributionChannel> distributionChannels) {
        this.distributionChannels = distributionChannels;
    }
}
