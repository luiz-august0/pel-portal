package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "sistema_avaliacao")
public class EvaluationSystemEntity extends BaseEntity {

    @Id
    @Column(name = "id_sistema_avaliacao")
    @SequenceGenerator(name = "id_sistema_avaliacao", sequenceName = "seq_sistema_avaliacao", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sistema_avaliacao")
    private Integer id;

    @Column(name = "nm_sistema_avaliacao", length = 40, nullable = false)
    private String evaluationSystemName;

    @Column(name = "vl_media_aprovacao")
    private BigDecimal approvalAverage;

    @Column(name = "pe_maximo_faltas")
    private BigDecimal maximumAbsencePercentage;

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
