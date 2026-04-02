package com.almeja.pel.portal.core.event.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(of = "cpf", callSuper = false)
public class EventPreRegistrationReviewDTO extends BaseDTO {

    private String cpf;

    private Boolean approved;

}
