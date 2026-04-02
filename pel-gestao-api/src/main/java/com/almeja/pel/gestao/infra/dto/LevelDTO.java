package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class LevelDTO extends BaseDTO {

    private Integer id;

    private CourseDTO course;

    private String levelName;

    private Integer levelCode;

    private String levelDescription;

    private Boolean allowsEntry;

    private Boolean allowsCompetition;

    private Boolean isGraduating;

    private Boolean isPostCompletion;

}
