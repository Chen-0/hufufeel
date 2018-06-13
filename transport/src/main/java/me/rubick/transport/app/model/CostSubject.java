package me.rubick.transport.app.model;

import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rubick_cost_subject")
public class CostSubject {
    private long id;
    private long userId;
    private String costSubjectSnapshot;
    private Date createdAt;
    private Date updatedAt;

//    @Transient
//    private CostSubjectSnapshotVo costSubjectSnapshotVo;

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

    @Column(name = "cost_subject_snapshot")
    public String getCostSubjectSnapshot() {
        return costSubjectSnapshot;
    }

    public void setCostSubjectSnapshot(String costSubjectSnapshot) {
        this.costSubjectSnapshot = costSubjectSnapshot;
    }

//    public CostSubjectSnapshotVo getCostSubjectSnapshotVo() {
//        return costSubjectSnapshotVo;
//    }
//
//    public void setCostSubjectSnapshotVo(CostSubjectSnapshotVo costSubjectSnapshotVo) {
//        this.costSubjectSnapshotVo = costSubjectSnapshotVo;
//    }
}
