package com.almeja.pel.gestao.core.exception.enums;

import lombok.Getter;

@Getter
public enum EnumAppException {
    GENERATE_TOKEN("Ocorreu um erro ao gerar o token de acesso"),
    VALIDATE_TOKEN("Ocorreu um erro ao validar o token"),
    EXPIRED_SESSION("Sessão expirada, realize o login novamente"),
    USER_NOT_FOUND("Usuário não encontrado");

    private final String message;

    EnumAppException(String message) {
        this.message = message;
    }

}
