package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class LevelingScheduleDTO extends BaseDTO {

    private Integer id;

    private CourseDTO course;

    private Date levelingDate;

    private Integer availableSlots;

}
