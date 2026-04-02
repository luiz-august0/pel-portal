package com.almeja.pel.gestao.infra.dto.mapper;

import com.almeja.pel.gestao.infra.dto.GradeByQuarterDTO;
import com.almeja.pel.gestao.infra.dto.GradeDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;

import java.util.List;

public class GradeByQuarterMapper {

    public static List<GradeByQuarterDTO> toDTO(List<com.almeja.pel.gestao.core.dto.GradeByQuarterDTO> gradeByQuarter) {
        return gradeByQuarter.stream().map(quarter -> new GradeByQuarterDTO(
                quarter.getQuarter(),
                quarter.getGrade(),
                ConverterEntityToDTOUtil.convert(quarter.getGrades(), GradeDTO.class))).toList();
    }

}
