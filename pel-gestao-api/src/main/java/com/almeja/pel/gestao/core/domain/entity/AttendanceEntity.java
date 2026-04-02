package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumAttendancePresentType;
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
@Table(name = "frequencia")
public class AttendanceEntity extends BaseEntity {

    @Id
    @Column(name = "id_frequencia")
    @SequenceGenerator(name = "id_frequencia", sequenceName = "seq_frequencia", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_frequencia")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plano_aula", nullable = false)
    private LessonPlanEntity lessonPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matricula", nullable = false)
    private InscriptionEntity inscription;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "in_presente")
    private EnumAttendancePresentType isPresent;

    @Column(name = "in_presente2")
    private EnumAttendancePresentType isPresent2;

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
