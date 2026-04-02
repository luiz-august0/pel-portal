package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class AddressValidatorService {

    private static final List<String> VALID_STATES = Arrays.asList(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", 
        "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", 
        "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public static void validateCep(String cep) {
        if (StringUtil.isNullOrEmpty(cep)) {
            throw new ValidatorException("CEP é obrigatório");
        }

        // Remove formatação se houver
        String cleanCep = cep.replaceAll("[^0-9]", "");
        
        if (cleanCep.length() != 8) {
            throw new ValidatorException("CEP deve conter exatamente 8 dígitos");
        }

        if (!cleanCep.matches("\\d{8}")) {
            throw new ValidatorException("CEP deve conter apenas números");
        }
    }

    public static void validateStreet(String street) {
        if (StringUtil.isNullOrEmpty(street)) {
            throw new ValidatorException("Rua é obrigatória");
        }

        if (street.length() > 255) {
            throw new ValidatorException("Rua deve ter no máximo 255 caracteres");
        }
    }

    public static void validateNumber(String number) {
        if (StringUtil.isNullOrEmpty(number)) {
            throw new ValidatorException("Número é obrigatório");
        }

        if (number.length() > 20) {
            throw new ValidatorException("Número deve ter no máximo 20 caracteres");
        }
    }

    public static void validateComplement(String complement) {
        if (complement != null && complement.length() > 100) {
            throw new ValidatorException("Complemento deve ter no máximo 100 caracteres");
        }
    }

    public static void validateNeighborhood(String neighborhood) {
        if (StringUtil.isNullOrEmpty(neighborhood)) {
            throw new ValidatorException("Bairro é obrigatório");
        }

        if (neighborhood.length() > 100) {
            throw new ValidatorException("Bairro deve ter no máximo 100 caracteres");
        }
    }

    public static void validateCity(String city) {
        if (StringUtil.isNullOrEmpty(city)) {
            throw new ValidatorException("Cidade é obrigatória");
        }

        if (city.length() > 100) {
            throw new ValidatorException("Cidade deve ter no máximo 100 caracteres");
        }
    }

    public static void validateState(String state) {
        if (StringUtil.isNullOrEmpty(state)) {
            throw new ValidatorException("Estado é obrigatório");
        }

        String upperState = state.toUpperCase();
        if (!VALID_STATES.contains(upperState)) {
            throw new ValidatorException("Estado deve ser uma sigla válida de estado brasileiro");
        }
    }

}
