package com.almeja.pel.gestao.infra.mediator;

import com.almeja.pel.gestao.core.mediator.Command;
import com.almeja.pel.gestao.core.mediator.CommandHandler;
import com.almeja.pel.gestao.core.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação do Mediator usando Spring Context para descobrir handlers
 * Roteia commands para seus respectivos handlers automaticamente
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MediatorImpl implements Mediator {

    private final ApplicationContext applicationContext;
    private final Map<Class<?>, CommandHandler<?, ?>> handlerCache = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T send(Command<T> command) {
        CommandHandler<Command<T>, T> handler = (CommandHandler<Command<T>, T>) getHandler(command.getClass());
        if (handler == null)
            throw new IllegalArgumentException("Nenhum handler encontrado para command: " + command.getClass().getSimpleName());
        return handler.handle(command);
    }

    private CommandHandler<?, ?> getHandler(Class<?> commandClass) {
        return handlerCache.computeIfAbsent(commandClass, this::findHandler);
    }

    @SuppressWarnings("rawtypes")
    private CommandHandler<?, ?> findHandler(Class<?> commandClass) {
        Map<String, CommandHandler> handlers = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler<?, ?> handler : handlers.values()) {
            Class<?> handlerCommandClass = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (handlerCommandClass != null && handlerCommandClass.isAssignableFrom(commandClass)) return handler;
        }
        throw new RuntimeException("Nenhum handler encontrado para command: " + commandClass.getSimpleName());
    }

}
