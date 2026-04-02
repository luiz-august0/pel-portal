package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class LessonPlanDTO extends BaseDTO {

    private Integer id;

    private String activityName;

    private String activityDescription;

    private Date completedDate;

    private Boolean isExtraClassMakeup;

    private String unitName;

}
