package com.almeja.pel.portal.core.domain.enums;

import com.almeja.pel.portal.core.domain.enums.base.IEnum;
import com.almeja.pel.portal.core.domain.enums.base.IEnumConverter;
import lombok.Getter;

@Getter
public enum EnumDependentRelationship implements IEnum {
    SON("SON", "Filho", "Pai/Mãe"),
    NEPHEW("NEPHEW", "Sobrinho", "Tio/Tia"),
    STEPSON("STEPSON", "Enteado", "Padrasto/Madrasta"),
    GRANDCHILD("GRANDCHILD", "Neto", "Avô/Avó");

    private final String key;
    private final String value;
    private final String otherPart;

    EnumDependentRelationship(String key, String value, String otherPart) {
        this.key = key;
        this.value = value;
        this.otherPart = otherPart;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter implements IEnumConverter<EnumDependentRelationship, String> {

        @Override
        public EnumDependentRelationship[] getValues() {
            return EnumDependentRelationship.values();
        }

    }

}
