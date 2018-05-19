package me.rubick.transport.app.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

/**
 * Created by chenjiazhuo on 2017/9/19.
 */
@Entity
@Table(name = "authority")
public class Role implements GrantedAuthority {

    public final static Role DEFAULT_ROLE = new Role(3, "ROLE_DEFAULT", "ROLE_DEFAULT");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String authority;

    public Role() {}

    public Role(long id, String name, String authority) {
        this.id = id;
        this.name = name;
        this.authority = authority;
    }

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
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
