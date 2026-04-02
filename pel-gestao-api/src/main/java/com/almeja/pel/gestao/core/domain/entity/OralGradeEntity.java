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
@Table(name = "nota_oral")
public class OralGradeEntity extends BaseEntity {

    @Id
    @Column(name = "id_nota_oral")
    @SequenceGenerator(name = "id_nota_oral", sequenceName = "seq_nota_oral", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_nota_oral")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso_avaliacao", nullable = false)
    private CourseEvaluationEntity courseEvaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matricula", nullable = false)
    private InscriptionEntity inscription;

    @Column(name = "vl_nota_vocabulario")
    private BigDecimal vocabularyGrade;

    @Column(name = "vl_nota_gramatica")
    private BigDecimal grammarGrade;

    @Column(name = "vl_nota_pronuncia")
    private BigDecimal pronunciationGrade;

    @Column(name = "vl_nota_fluencia")
    private BigDecimal fluencyGrade;

    @Column(name = "vl_nota_comunicacao")
    private BigDecimal communicationGrade;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "in_nao_realizou")
    private Boolean notPerformed;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
        if (this.notPerformed == null) this.notPerformed = false;
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
