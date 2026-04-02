package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class ClassDTO extends BaseDTO {

    private Integer id;

    private CourseDTO course;

    private LevelDTO level;

    @ObjectFieldsOnly({"id", "name"})
    private PersonDTO professor;

    private String dayOfWeek;

    private Integer availableSlots;

    private Date startTime;

    private Date endTime;

    private String className;

    private Date plannedStartDate;

    private Boolean inactive;

    private String roomName;

    private Boolean closed;

    private Date plannedEndDate;

    private Integer subscribers;

}
