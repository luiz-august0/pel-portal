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
@Table(name = "curso_avaliacao")
public class CourseEvaluationEntity extends BaseEntity {

    @Id
    @Column(name = "id_curso_avaliacao")
    @SequenceGenerator(name = "id_curso_avaliacao", sequenceName = "seq_curso_avaliacao", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_curso_avaliacao")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CourseEntity course;

    @Column(name = "nm_curso_avaliacao", nullable = false, length = 40)
    private String courseEvaluationName;

    @Column(name = "nr_agrupamento", nullable = false)
    private Integer groupNumber;

    @Column(name = "pe_peso", nullable = false)
    private BigDecimal weight;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "in_oral")
    private Boolean isOral;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
        if (this.isOral == null) this.isOral = false;
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
