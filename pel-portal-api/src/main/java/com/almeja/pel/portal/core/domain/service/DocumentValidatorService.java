package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.exception.ValidatorException;

import java.math.BigDecimal;

public class DocumentValidatorService {

    public static void validate(UserEntity user, EnumDocumentType documentType, String filename, String originalFilename, BigDecimal size) {
        if (user == null) throw new ValidatorException("Usuário é obrigatório");
        if (documentType == null) throw new ValidatorException("Tipo do documento é obrigatório");
        if (filename == null || filename.trim().isEmpty()) throw new ValidatorException("Nome do arquivo é obrigatório");
        if (originalFilename == null || originalFilename.trim().isEmpty()) throw new ValidatorException("Nome original do arquivo é obrigatório");
        if (size == null) throw new ValidatorException("Tamanho do arquivo é obrigatório");
    }

}
