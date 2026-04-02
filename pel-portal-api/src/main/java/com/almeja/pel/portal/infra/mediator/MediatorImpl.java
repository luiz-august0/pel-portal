package com.almeja.pel.portal.infra.mediator;

import com.almeja.pel.portal.core.mediator.AsyncCommandHandler;
import com.almeja.pel.portal.core.mediator.Command;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    private final Map<Class<?>, AsyncCommandHandler<?, ?>> asyncHandlerCache = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T send(Command<T> command) {
        CommandHandler<Command<T>, T> handler = (CommandHandler<Command<T>, T>) getHandler(command.getClass());
        if (handler == null) throwHandlerNotFoundException(command);
        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendAsync(Command<T> command) {
        AsyncCommandHandler<Command<T>, T> handler = (AsyncCommandHandler<Command<T>, T>) getAsyncHandler(command.getClass());
        if (handler == null) throwHandlerNotFoundException(command);
        handler.handleAsync(command);
    }

    private CommandHandler<?, ?> getHandler(Class<?> commandClass) {
        return handlerCache.computeIfAbsent(commandClass, this::findHandler);
    }

    private AsyncCommandHandler<?, ?> getAsyncHandler(Class<?> commandClass) {
        return asyncHandlerCache.computeIfAbsent(commandClass, this::findAsyncHandler);
    }

    @SuppressWarnings("rawtypes")
    private CommandHandler<?, ?> findHandler(Class<?> commandClass) {
        Map<String, CommandHandler> handlers = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler<?, ?> handler : handlers.values()) {
            Class<?> handlerCommandClass = getCommandClassFromHandler(handler.getClass(), CommandHandler.class);
            if (handlerCommandClass != null && handlerCommandClass.equals(commandClass)) return handler;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private AsyncCommandHandler<?, ?> findAsyncHandler(Class<?> commandClass) {
        Map<String, AsyncCommandHandler> handlers = applicationContext.getBeansOfType(AsyncCommandHandler.class);
        for (AsyncCommandHandler<?, ?> handler : handlers.values()) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
            Class<?> handlerCommandClass = getCommandClassFromHandler(targetClass, AsyncCommandHandler.class);
            if (handlerCommandClass != null && handlerCommandClass.equals(commandClass)) return handler;
        }
        return null;
    }

    /**
     * Extrai a classe concreta do command a partir da implementação do handler
     * Busca nas interfaces implementadas pelo handler para encontrar o tipo parametrizado
     */
    private Class<?> getCommandClassFromHandler(Class<?> handlerClass, Class<?> targetInterface) {
        // Busca nas interfaces implementadas
        Type[] genericInterfaces = handlerClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                // Verifica se é a interface CommandHandler ou AsyncCommandHandler
                if (rawType.equals(targetInterface)) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                        return (Class<?>) typeArguments[0];
                    }
                }
            }
        }
        // Se não encontrou nas interfaces diretas, busca na superclasse
        Class<?> superclass = handlerClass.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            return getCommandClassFromHandler(superclass, targetInterface);
        }
        return null;
    }

    private static <T> void throwHandlerNotFoundException(Command<T> command) {
        throw new IllegalArgumentException("Nenhum handler encontrado para command: " + command.getClass().getSimpleName());
    }

}
