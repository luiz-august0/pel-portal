package com.almeja.pel.portal.core.domain.enums;

import com.almeja.pel.portal.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumInternalRelationshipType implements IEnum {
    ACADEMIC_GRADUATION("ACADEMIC_GRADUATION", "Acadêmico gradução"),
    ACADEMIC_POS_GRADUATION("ACADEMIC_POS_GRADUATION", "Acadêmico pós-graduação"),
    UNIVERSITY_STAFF("UNIVERSITY_STAFF", "Funcionário universitário"),
    ACTIVE_TEACHING_STAFF("ACTIVE_TEACHING_STAFF", "Funcionário docente ativo"),
    TEMPORARY_TEACHING_STAFF("TEMPORARY_TEACHING_STAFF", "Funcionário docente temporário"),
    PARANA_STATE_SERVER("PARANA_STATE_SERVER", "Servidor do estado do Paraná");

    private final String key;
    private final String value;

    EnumInternalRelationshipType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumInternalRelationshipType, String> {

        @Override
        public EnumInternalRelationshipType[] getValues() {
            return EnumInternalRelationshipType.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumInternalRelationshipType, String>> getConverter() {
        return EnumInternalRelationshipType.Converter.class;
    }

}
