package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "arquivo")
public class FileEntity extends BaseEntity {

    @Id
    @Column(name = "id_arquivo")
    @SequenceGenerator(name = "id_arquivo", sequenceName = "seq_arquivo", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_arquivo")
    private Integer id;

    @Column(name = "ds_arquivo", length = 100)
    private String description;

    @Column(name = "nm_arquivo", length = 100)
    private String fileName;

    @Column(name = "tp_mimetype", length = 100)
    private String mimeType;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "aq_arquivo")
    private byte[] fileData;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        onSave();
    }

    @PreUpdate
    public void preUpdate() {
        onSave();
    }

}
