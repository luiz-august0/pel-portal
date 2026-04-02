package com.almeja.pel.gestao.core.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class PortalUserDTO extends BaseDTO {

    private UUID id;

    private String name;

    private String email;

    private String cpf;

    private PortalUserDetailsDTO userDetails;

    private PortalAddressDTO address;

    private List<PortalDocumentDTO> documents;

    private PortalUserDTO responsible;

}
