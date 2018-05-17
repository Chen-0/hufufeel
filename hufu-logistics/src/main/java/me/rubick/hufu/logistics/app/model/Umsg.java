package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "think_umsg")
public class Umsg {

    @Column(name = "uid")
    private Integer uid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time")
    private Date time;

    @Column(name = "content")
    private String content;

    @Column(name = "read_flag")
    private Boolean readFlag;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getRead_flag() {
        return readFlag;
    }

    public void setRead_flag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

}

