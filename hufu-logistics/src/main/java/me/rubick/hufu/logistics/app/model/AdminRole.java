package me.rubick.hufu.logistics.app.model;

import org.springframework.security.core.GrantedAuthority;

//@Entity
//@Table(name = "role")
public class AdminRole implements GrantedAuthority {
    AdminRole() {
    }

    AdminRole(String authority) {
        this.authority = authority;
    }

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(name = "name", nullable = false)
//    @NotNull
    private String name;

    //    @Column(name = "authority", nullable = false)
//    @NotNull
    private String authority;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    @Override
    public String toString() {
        return "[Name:" + name + ", authority:" + authority + "]";
    }

}