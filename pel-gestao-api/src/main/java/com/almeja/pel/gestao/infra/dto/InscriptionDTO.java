package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionResult;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionStatus;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionType;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class InscriptionDTO extends BaseDTO {

    private Integer id;

    @ObjectFieldsOnly({"id", "name"})
    private PersonDTO student;

    private ClassDTO clazz;

    private EnumInscriptionStatus status;

    private String observation;

    private String lastUpdateUser;

    private Date lastUpdateDate;

    private BigDecimal attendancePercentage;

    private BigDecimal finalGrade;

    private EnumInscriptionResult result;

    private String certificateCode;

    private Date inscriptionDate;

    private Date exitDate;

    private String contractText;

    private Boolean closed;

    private String imageTermDescription;

    private EnumInscriptionType inscriptionType;

    private Boolean inscriptionFinalized;

    private String fileContractName;

    private String fileCertificateName;

}
