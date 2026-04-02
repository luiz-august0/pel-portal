package com.almeja.pel.portal.core.util;

import com.almeja.pel.portal.core.annotation.ObjectFieldsOnly;
import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import com.almeja.pel.portal.core.exception.AppException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.lang.reflect.Field;
import java.util.*;

public class ConverterEntityToDTOUtil {

    @SuppressWarnings("unchecked")
    public static <Entity extends BaseEntity, DTO extends BaseDTO> DTO convert(Entity entity, Class<DTO> dtoClass) {
        try {
            if (entity == null) return null;

            DTO dtoObj = dtoClass.getDeclaredConstructor().newInstance();
            List<Field> dtoFields = getAllFields(dtoClass).stream().filter(ConverterEntityToDTOUtil::doFilterAnnotations).toList();

            mapperEntityFieldsToDTOFields((Entity) Hibernate.unproxy(entity), dtoObj, dtoFields);

            return dtoObj;
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public static <Entity extends BaseEntity, DTO extends BaseDTO> List<DTO> convert(List<Entity> entityList, Class<DTO> dtoClass) {
        return entityList.stream().map(entity -> convert(entity, dtoClass)).toList();
    }

    public static <Entity extends BaseEntity, DTO extends BaseDTO> Page<DTO> convert(Page<Entity> entityPage, Class<DTO> dtoClass) {
        return new PageImpl<>(
                entityPage.stream().map(entity -> convert(entity, dtoClass)).toList(),
                entityPage.getPageable(),
                entityPage.getTotalElements()
        );
    }

    @SuppressWarnings("unchecked")
    private static <Entity extends BaseEntity, DTO extends BaseDTO> DTO mapperEntityFieldsToDTOFields(Entity entity, DTO dtoObj, List<Field> dtoFields) {
        dtoFields.forEach(dtoField -> {
            try {
                Field entityField = getFieldFromClassHierarchy(entity.getClass(), dtoField.getName());
                entityField.setAccessible(true);
                dtoField.setAccessible(true);
                Object entityFieldValue = entityField.get(entity);

                if (entityFieldValue instanceof BaseEntity) {
                    dtoField.set(dtoObj, getDTOValueFromDTOField((Entity) Hibernate.unproxy(entityFieldValue), (Class<DTO>) dtoField.getType(), dtoField));
                } else if (entityFieldValue instanceof Collection<?> entityCollection) {
                    entityCollection = (Collection<?>) Hibernate.unproxy(entityCollection);
                    if (!entityCollection.isEmpty()) {
                        Class<?> entityCollectionClass = ClassUtil.getClassFromCollectionField(entityField);

                        if (entityCollectionClass.getSuperclass().equals(BaseEntity.class)) {
                            Class<DTO> dtoCollectionClass = (Class<DTO>) ClassUtil.getClassFromCollectionField(dtoField);
                            Collection<DTO> dtoCollection;

                            if (entityCollection instanceof HashSet<?>) {
                                dtoCollection = new HashSet<>();
                            } else {
                                dtoCollection = new ArrayList<>();
                            }

                            entityCollection.forEach(entityCollectionFieldValue -> {
                                dtoCollection.add(getDTOValueFromDTOField((Entity) entityCollectionFieldValue, dtoCollectionClass, dtoField));
                            });

                            dtoField.set(dtoObj, dtoCollection);
                        } else {
                            dtoField.set(dtoObj, entityFieldValue);
                        }
                    }
                } else {
                    dtoField.set(dtoObj, entityFieldValue);
                }

            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
        });

        return dtoObj;
    }

    private static <Entity extends BaseEntity, DTO extends BaseDTO> DTO getDTOValueFromDTOField(Entity entity, Class<DTO> dtoClass, Field dtoField) {
        try {
            String[] DTOFields;
            String[] DTOFieldsToIgnore;

            if (dtoField.isAnnotationPresent(ObjectFieldsOnly.class)) {
                DTOFields = dtoField.getAnnotation(ObjectFieldsOnly.class).value();
                DTOFieldsToIgnore = dtoField.getAnnotation(ObjectFieldsOnly.class).ignored();
            } else {
                DTOFields = new String[0];
                DTOFieldsToIgnore = new String[0];
            }

            DTO dtoFieldObj = dtoClass.getDeclaredConstructor().newInstance();
            List<Field> dtoFields = getAllFields(dtoClass).stream().filter(field ->
                    doFilterAnnotations(field) && (doDTOFieldsFilter(DTOFields, field)) && (doDTOFieldsToIgnoreFilter(DTOFieldsToIgnore, field))
            ).toList();

            return mapperEntityFieldsToDTOFields(entity, dtoFieldObj, dtoFields);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    private static boolean doFilterAnnotations(Field dtoField) {
        return ((!dtoField.isAnnotationPresent(JsonProperty.class)) ||
                (dtoField.isAnnotationPresent(JsonProperty.class) && !dtoField.getAnnotation(JsonProperty.class).access().equals(JsonProperty.Access.WRITE_ONLY))) &&
                (!dtoField.isAnnotationPresent(JsonIgnore.class));
    }

    private static boolean doDTOFieldsFilter(String[] DTOFields, Field dtoField) {
        return DTOFields.length == 0 || Arrays.stream(DTOFields).anyMatch(DTOField -> DTOField.equals(dtoField.getName()));
    }

    private static boolean doDTOFieldsToIgnoreFilter(String[] DTOFieldsToIgnore, Field dtoField) {
        return DTOFieldsToIgnore.length == 0 || Arrays.stream(DTOFieldsToIgnore).noneMatch(DTOField -> DTOField.equals(dtoField.getName()));
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }

    private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Campo '" + fieldName + "' não encontrado na hierarquia de classes de " + clazz.getSimpleName());
    }

}