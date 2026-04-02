package com.almeja.pel.portal.core.exception.enums;

import lombok.Getter;

@Getter
public enum EnumAppException {
    GENERATE_TOKEN("Ocorreu um erro ao gerar o token de acesso"),
    GENERATE_RECOVERY_TOKEN("Ocorreu um erro ao gerar o token de recuperação"),
    GENERATE_RESPONSIBLE_LINK("Ocorreu um erro ao gerar o link para o responsável"),
    VALIDATE_TOKEN("Ocorreu um erro ao validar o token"),
    EXPIRED_SESSION("Sessão expirada, realize o login novamente"),
    USER_NOT_FOUND("Usuário não encontrado"),
    USER_WITHOUT_EMAIL("Usuário sem e-mail cadastrado"),
    EXPIRED_RECOVERY("Recuperação expirada, realize a solicitação novamente"),
    EXPIRED_RESPONSIBLE_LINK("Link para responsável expirado, realize a solicitação novamente"),
    EXPIRED_DEPENDENT_LINK("Link para dependente expirado, realize a solicitação novamente"),
    USER_INACTIVE("Usuário não está ativo"),
    WRONG_CREDENTIALS("E-mail ou senha incorretos");

    private final String message;

    EnumAppException(String message) {
        this.message = message;
    }

}
