package me.rubick.hufu.logistics.app.model;

import javax.persistence.*;

@Entity
@Table(name = "think_article")
public class Article {

    @Column(name = "createtime")
    private Integer createtime;

    @Column(name = "author")
    private String author;

    @Column(name = "subject")
    private String subject;

    @Column(name = "lastmodifytime")
    private Integer lastmodifytime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "message")
    private String message;

    public Integer getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getLastmodifytime() {
        return lastmodifytime;
    }

    public void setLastmodifytime(Integer lastmodifytime) {
        this.lastmodifytime = lastmodifytime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

