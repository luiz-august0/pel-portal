package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "turma")
public class ClassEntity extends BaseEntity {

    @Id
    @Column(name = "id_turma")
    @SequenceGenerator(name = "id_turma", sequenceName = "seq_turma", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_turma")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel", nullable = false)
    private LevelEntity level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor")
    private PersonEntity professor;

    @Column(name = "di_semana", nullable = false, length = 30)
    private String dayOfWeek;

    @Column(name = "qt_vagas", nullable = false)
    private Integer availableSlots;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "hr_inicio")
    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Column(name = "hr_termino")
    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Column(name = "nm_turma", nullable = false, length = 20)
    private String className;

    @Column(name = "dt_inicio_previsto")
    @Temporal(TemporalType.DATE)
    private Date plannedStartDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publico")
    private PublicEntity publicEntity;

    @Column(name = "in_inativa", nullable = false)
    private Boolean inactive;

    @Column(name = "nm_sala", length = 20)
    private String roomName;

    @Column(name = "in_fechado", nullable = false)
    private Boolean closed;

    @Column(name = "dt_termino_previsto")
    @Temporal(TemporalType.DATE)
    private Date plannedEndDate;

    @Transient
    private Integer subscribers;

    public String getExtensionDayOfWeek() {
        if (this.dayOfWeek == null || this.dayOfWeek.isEmpty()) {
            return "";
        }
        Map<String, String> dayMap = Map.of(
                "SEG", "Segunda",
                "TER", "Terça",
                "QUA", "Quarta",
                "QUI", "Quinta",
                "SEX", "Sexta",
                "SAB", "Sábado",
                "DOM", "Domingo"
        );
        if (!this.dayOfWeek.contains(";")) {
            return dayMap.getOrDefault(this.dayOfWeek.toUpperCase().trim(), this.dayOfWeek);
        }
        String[] dayArray = this.dayOfWeek.split(";");
        List<String> days = new ArrayList<>();
        for (String day : dayArray) {
            String trimmedDay = day.trim().toUpperCase();
            days.add(dayMap.getOrDefault(trimmedDay, trimmedDay));
        }
        if (days.size() == 1) {
            return days.getFirst();
        }
        if (days.size() == 2) {
            return days.get(0) + " e " + days.get(1);
        }
        String lastDay = days.removeLast(); // Remove and get the last day
        return String.join(", ", days) + " e " + lastDay;
    }

    public boolean isPersonAgePermitted(PersonEntity person) {
        if (this.publicEntity == null) return true;
        return this.publicEntity.getMinimumAge() <= person.getAge() && this.publicEntity.getMaximumAge() >= person.getAge();
    }

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
        if (this.inactive == null) this.inactive = false;
        if (this.closed == null) this.closed = false;
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
