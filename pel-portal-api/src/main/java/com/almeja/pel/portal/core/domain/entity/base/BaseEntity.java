package com.almeja.pel.portal.core.domain.entity.base;

import com.almeja.pel.portal.core.util.DateUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = DateUtil.getDate();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = DateUtil.getDate();
    }

}
