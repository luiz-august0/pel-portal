package com.almeja.pel.gestao.core.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumStudyLanguageTime;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterLevelingDTO extends BaseDTO {

    private Integer courseId;

    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddHH:mm:ss", timezone = "America/Sao_Paulo")
    private Date levelingDate;

    private EnumStudyLanguageTime studyLanguageTime;

}