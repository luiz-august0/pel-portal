package com.almeja.pel.gestao.core.domain.enums.base;

public interface IEnum {

    Object getKey();

    String getValue();

    Class<? extends Converter<?, ?>> getConverter();

    abstract class Converter<ConcreteEnum extends IEnum, Object> implements IEnumConverter<ConcreteEnum, Object> {}

}
