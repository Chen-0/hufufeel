package me.rubick.transport.app.model;

import me.rubick.transport.app.vo.UserCsVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "think_company")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private BigDecimal usd;

    @Column
    private boolean arrearage;

    //客户编号
    @Column(name = "hwc_sn")
    private String hwcSn;

    @Column(name = "cs_info")
    private String csInfo;

    @Transient
    private UserCsVo userCsVo;
//
//    @Column
//    private int ctype;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_authority",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Boolean isAdmin() {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }

        return false;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public boolean isArrearage() {
        return arrearage;
    }

    public void setArrearage(boolean arrearage) {
        this.arrearage = arrearage;
    }

    public String getHwcSn() {
        return hwcSn;
    }

    public void setHwcSn(String hwcSn) {
        this.hwcSn = hwcSn;
    }

    public String getCsInfo() {
        return csInfo;
    }

    public void setCsInfo(String csInfo) {
        this.csInfo = csInfo;
    }

    @Transient
    public UserCsVo getUserCsVo() {
        return userCsVo;
    }

    public void setUserCsVo(UserCsVo userCsVo) {
        this.userCsVo = userCsVo;
    }
//
//    public int getCtype() {
//        return ctype;
//    }
//
//    public void setCtype(int ctype) {
//        this.ctype = ctype;
//    }
}
