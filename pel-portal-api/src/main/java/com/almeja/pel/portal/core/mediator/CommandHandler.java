package com.almeja.pel.portal.core.mediator;

/**
 * Interface base para todos os handlers de commands do Mediator
 * Handlers são responsáveis por processar commands específicos
 */
public interface CommandHandler<C extends Command<T>, T> {

    /**
     * Processa o command e retorna o resultado
     *
     * @param command Command a ser processado
     * @return Resultado do processamento
     */
    T handle(C command);
}
