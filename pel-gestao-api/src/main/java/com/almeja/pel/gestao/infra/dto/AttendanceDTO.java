package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumAttendancePresentType;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class AttendanceDTO extends BaseDTO {

    private Integer id;

    private LessonPlanDTO lessonPlan;

    private EnumAttendancePresentType isPresent;

    private EnumAttendancePresentType isPresent2;

}
