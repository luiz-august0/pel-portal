package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class CourseDTO extends BaseDTO {

    private Integer id;

    private String courseName;

    private String certificateCourseName;

}
