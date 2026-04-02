package com.almeja.pel.gestao.core.mediator;

/**
 * Interface do Mediator para envio de commands
 * Responsável por rotear commands para seus respectivos handlers
 */
public interface Mediator {
    
    /**
     * Envia um command para ser processado pelo handler apropriado
     * @param command Command a ser processado
     * @return Resultado do processamento
     */
    <T> T send(Command<T> command);
}
