package com.almeja.pel.gestao.core.dto.enums;

import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import lombok.Getter;

@Getter
public enum EnumPortalInternalRelationshipType {
    ACADEMIC_GRADUATION("ACADEMIC_GRADUATION", "Acadêmico gradução", EnumBondType.IAG),
    ACADEMIC_POS_GRADUATION("ACADEMIC_POS_GRADUATION", "Acadêmico pós-graduação", EnumBondType.IAP),
    UNIVERSITY_STAFF("UNIVERSITY_STAFF", "Funcionário universitário", EnumBondType.IAU),
    ACTIVE_TEACHING_STAFF("ACTIVE_TEACHING_STAFF", "Funcionário docente ativo", EnumBondType.IDE),
    TEMPORARY_TEACHING_STAFF("TEMPORARY_TEACHING_STAFF", "Funcionário docente temporário", EnumBondType.IDT),
    PARANA_STATE_SERVER("PARANA_STATE_SERVER", "Servidor do estado do Paraná", EnumBondType.ISP);

    private final String key;
    private final String value;
    private final EnumBondType bondType;

    EnumPortalInternalRelationshipType(String key, String value, EnumBondType bondType) {
        this.key = key;
        this.value = value;
        this.bondType = bondType;
    }

}
