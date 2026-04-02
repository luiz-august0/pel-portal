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
@Table(name = "curso")
public class CourseEntity extends BaseEntity {

    @Id
    @Column(name = "id_curso")
    @SequenceGenerator(name = "id_curso", sequenceName = "seq_curso", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_curso")
    private Integer id;

    @Column(name = "nm_curso", length = 60, nullable = false)
    private String courseName;

    @Column(name = "nm_curso_certificado", length = 100)
    private String certificateCourseName;

    @Column(name = "in_inativo", nullable = false)
    private Boolean inactive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema_avaliacao")
    private EvaluationSystemEntity evaluationSystem;

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
