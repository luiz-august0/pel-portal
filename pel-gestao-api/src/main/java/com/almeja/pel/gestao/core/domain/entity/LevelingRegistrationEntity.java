package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumStudyLanguageTime;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "marcacao_nivelamento")
public class LevelingRegistrationEntity extends BaseEntity {

    @Id
    @Column(name = "id_marcacao_nivelamento")
    @SequenceGenerator(name = "id_marcacao_nivelamento", sequenceName = "seq_marcacao_nivelamento", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_marcacao_nivelamento")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private PersonEntity person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario_nivelamento", nullable = false)
    private LevelingScheduleEntity levelingSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_aprovado")
    private LevelEntity approvedLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliador")
    private PersonEntity evaluator;

    @Column(name = "tempo_estudo_idioma")
    private EnumStudyLanguageTime studyLanguageTime;

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

    public LevelingRegistrationEntity(PersonEntity person, LevelingScheduleEntity levelingSchedule, CourseEntity course, EnumStudyLanguageTime studyLanguageTime) {
        this.person = person;
        this.levelingSchedule = levelingSchedule;
        this.course = course;
        this.studyLanguageTime = studyLanguageTime;
    }
    
}
