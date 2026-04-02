package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserStatusDTO extends BaseDTO {

    private String description;

    private boolean checked;

    private boolean optional;

    public UserStatusDTO(String description, boolean checked, boolean optional) {
        this.description = description;
        this.checked = checked;
        this.optional = optional;
    }

    public UserStatusDTO(String description, boolean checked) {
        this.description = description;
        this.checked = checked;
    }

}
