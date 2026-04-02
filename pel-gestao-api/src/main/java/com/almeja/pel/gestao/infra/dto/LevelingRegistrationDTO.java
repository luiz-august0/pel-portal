package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumStudyLanguageTime;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class LevelingRegistrationDTO extends BaseDTO {

    private Integer id;

    private LevelingScheduleDTO levelingSchedule;

    private CourseDTO course;

    private LevelDTO approvedLevel;

    @ObjectFieldsOnly({"id", "name"})
    private PersonDTO evaluator;

    private EnumStudyLanguageTime studyLanguageTime;

}
