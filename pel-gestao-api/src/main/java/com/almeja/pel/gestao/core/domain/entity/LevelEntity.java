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
@Table(name = "nivel")
public class LevelEntity extends BaseEntity {

    @Id
    @Column(name = "id_nivel")
    @SequenceGenerator(name = "id_nivel", sequenceName = "seq_nivel", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_nivel")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CourseEntity course;

    @Column(name = "nm_nivel", length = 40, nullable = false)
    private String levelName;

    @Column(name = "cd_nivel")
    private Integer levelCode;

    @Column(name = "ds_nivel")
    private String levelDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proximo_nivel")
    private LevelEntity nextLevel;

    @Column(name = "in_permite_ingresso", nullable = false)
    private Boolean allowsEntry = true;

    @Column(name = "in_permite_concorrencia")
    private Boolean allowsCompetition = false;

    @Column(name = "in_concluinte", nullable = false)
    private Boolean isGraduating = false;

    @Column(name = "in_pos_conclusao", nullable = false)
    private Boolean isPostCompletion = false;

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
