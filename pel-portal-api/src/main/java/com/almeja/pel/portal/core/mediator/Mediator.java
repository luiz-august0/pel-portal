package com.almeja.pel.portal.core.mediator;

/**
 * Interface do Mediator para envio de commands
 * Responsável por rotear commands para seus respectivos handlers
 */
public interface Mediator {
    
    /**
     * Envia um command para ser processado pelo handler apropriado de forma síncrona
     *
     * @param command Command a ser processado
     * @return Resultado do processamento
     */
    <T> T send(Command<T> command);

    /**
     * Envia um command para ser processado pelo handler apropriado de forma assíncrona
     *
     * @param command Command a ser processado
     */
    <T> void sendAsync(Command<T> command);

}
