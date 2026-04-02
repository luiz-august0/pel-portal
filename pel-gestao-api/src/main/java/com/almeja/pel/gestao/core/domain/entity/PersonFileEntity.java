package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumFileType;
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
@Table(name = "arquivo_pessoa")
public class PersonFileEntity extends BaseEntity {

    @Id
    @Column(name = "id_arquivo_pessoa")
    @SequenceGenerator(name = "id_arquivo_pessoa", sequenceName = "seq_arquivo_pessoa", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_arquivo_pessoa")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arquivo", nullable = false)
    private FileEntity file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private PersonEntity person;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11, nullable = false)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "tp_arquivo", length = 3, nullable = false)
    private EnumFileType fileType;

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
