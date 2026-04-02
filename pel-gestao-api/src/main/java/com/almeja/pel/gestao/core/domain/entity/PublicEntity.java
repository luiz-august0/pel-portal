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
@Table(name = "publico")
public class PublicEntity extends BaseEntity {

    @Id
    @Column(name = "id_publico")
    @SequenceGenerator(name = "id_publico", sequenceName = "seq_publico", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_publico")
    private Integer id;

    @Column(name = "nm_publico", length = 50, nullable = false)
    private String publicName;

    @Column(name = "qt_idade_minima")
    private Integer minimumAge;

    @Column(name = "qt_idade_maxima")
    private Integer maximumAge;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

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
