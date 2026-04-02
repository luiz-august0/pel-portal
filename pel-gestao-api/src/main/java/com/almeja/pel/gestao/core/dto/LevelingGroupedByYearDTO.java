package com.almeja.pel.gestao.core.dto;

import com.almeja.pel.gestao.core.domain.entity.LevelingRegistrationEntity;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LevelingGroupedByYearDTO extends BaseDTO {

    private String year;

    private List<LevelingRegistrationEntity> levelingRegistrations;

}
