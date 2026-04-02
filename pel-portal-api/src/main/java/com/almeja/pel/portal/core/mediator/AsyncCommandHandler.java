package com.almeja.pel.portal.core.mediator;

/**
 * Interface base para handlers assíncronos de commands do Mediator
 * Handlers assíncronos processam commands de forma não-bloqueante
 */
public interface AsyncCommandHandler<C extends Command<T>, T> {

    /**
     * Processa o command de forma assíncrona
     *
     * @param command Command a ser processado
     */
    void handleAsync(C command);

}
