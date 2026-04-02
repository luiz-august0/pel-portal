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
@Table(name = "config_matricula")
public class InscriptionConfigEntity extends BaseEntity {

    @Id
    @Column(name = "id_config_matricula")
    @SequenceGenerator(name = "id_config_matricula", sequenceName = "seq_config_matricula", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_config_matricula")
    private Integer id;

    @Column(name = "dt_inicio_matricula", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date inscriptionStartDate;

    @Column(name = "dt_termino_matricula", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date inscriptionEndDate;

    @Column(name = "dt_inicio_rematricula", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reinscriptionStartDate;

    @Column(name = "dt_termino_rematricula", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reinscriptionEndDate;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "vl_comunidade_interna", nullable = false)
    private BigDecimal internalCommunityValue;

    @Column(name = "vl_comunidade_externa", nullable = false)
    private BigDecimal externalCommunityValue;

    @Column(name = "tp_sistema_avaliacao", nullable = false)
    private String evaluationSystemType;

    @Column(name = "tx_contrato", columnDefinition = "TEXT")
    private String contractText;

    @Column(name = "in_permite_nivelamento", nullable = false)
    private Boolean allowsLeveling = false;

    @Column(name = "in_permite_pre_cadastro", nullable = false)
    private Boolean allowsPreRegistration = false;

    @Column(name = "in_bloqueia_nivelamento_aluno")
    private Boolean blocksStudentLeveling = false;

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
