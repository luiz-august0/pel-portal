package com.almeja.pel.portal.core.domain.enums.base;

import com.almeja.pel.portal.core.util.StringUtil;
import jakarta.persistence.AttributeConverter;

import java.util.stream.Stream;

public interface IEnumConverter<ConcreteEnum extends IEnum, Object> extends AttributeConverter<ConcreteEnum, Object> {

    ConcreteEnum[] getValues();

    @SuppressWarnings("unchecked")
    default Object convertToDatabaseColumn(ConcreteEnum concreteEnum) {
        if (concreteEnum != null) {
            return (Object) concreteEnum.getKey();
        }

        return null;
    }

    default ConcreteEnum convertToEntityAttribute(Object key) {
        if (key != null && StringUtil.isNotNullOrEmpty(key.toString())) {
            return Stream.of(getValues())
                    .filter(iEnum -> iEnum.getKey().toString().equals(key.toString()))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

}