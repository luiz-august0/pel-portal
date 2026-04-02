package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumTransferStatus;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TransferDTO extends BaseDTO {

    private Integer id;

    private Date requestDate;

    @ObjectFieldsOnly({"id", "clazz"})
    private InscriptionDTO sourceInscription;

    private ClassDTO destinationClass;

    private Date approvalDate;

    private EnumTransferStatus status;

    private String observation;

}
