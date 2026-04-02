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
@Table(name = "plano_aula")
public class LessonPlanEntity extends BaseEntity {

    @Id
    @Column(name = "id_plano_aula")
    @SequenceGenerator(name = "id_plano_aula", sequenceName = "seq_plano_aula", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_plano_aula")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma", nullable = false)
    private ClassEntity clazz;

    @Column(name = "nm_atividade", length = 200)
    private String activityName;
    
    @Column(name = "ds_atividade", nullable = false)
    private String activityDescription;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "dt_realizada")
    @Temporal(TemporalType.DATE)
    private Date completedDate;

    @Column(name = "in_reposicao_extra_classe")
    private Boolean isExtraClassMakeup;

    @Column(name = "nm_unidade", length = 50)
    private String unitName;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
        if (this.isExtraClassMakeup == null) this.isExtraClassMakeup = false;
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
