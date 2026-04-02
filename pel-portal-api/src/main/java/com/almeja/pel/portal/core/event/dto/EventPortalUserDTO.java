package com.almeja.pel.portal.core.event.dto;

import com.almeja.pel.portal.core.dto.AddressDTO;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.UserDetailsDTO;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class EventPortalUserDTO extends BaseDTO {

    private UUID id;

    private String name;

    private String email;

    private String cpf;

    private UserDetailsDTO userDetails;

    private AddressDTO address;

    private List<DocumentDTO> documents;

    private EventPortalUserDTO responsible;

}
