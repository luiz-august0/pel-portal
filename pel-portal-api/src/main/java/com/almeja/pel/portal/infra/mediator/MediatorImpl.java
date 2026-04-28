package com.almeja.pel.portal.infra.mediator;

import com.almeja.pel.portal.core.mediator.AsyncCommandHandler;
import com.almeja.pel.portal.core.mediator.Command;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.Mediator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Slf4j
public class MediatorImpl implements Mediator {

    @Inject
    @Any
    Instance<CommandHandler<?, ?>> handlers;

    @Inject
    @Any
    Instance<AsyncCommandHandler<?, ?>> asyncHandlers;

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

    private CommandHandler<?, ?> findHandler(Class<?> commandClass) {
        for (CommandHandler<?, ?> handler : handlers) {
            Class<?> handlerCommandClass = getCommandClassFromHandler(handler.getClass(), CommandHandler.class);
            if (handlerCommandClass != null && handlerCommandClass.equals(commandClass)) return handler;
        }
        return null;
    }

    private AsyncCommandHandler<?, ?> findAsyncHandler(Class<?> commandClass) {
        for (AsyncCommandHandler<?, ?> handler : asyncHandlers) {
            Class<?> targetClass = handler.getClass();
            // CDI proxies: look at the superclass to get the actual bean class
            if (targetClass.getName().contains("_Subclass") || targetClass.getName().contains("$$")) {
                targetClass = targetClass.getSuperclass();
            }
            Class<?> handlerCommandClass = getCommandClassFromHandler(targetClass, AsyncCommandHandler.class);
            if (handlerCommandClass != null && handlerCommandClass.equals(commandClass)) return handler;
        }
        return null;
    }

    private Class<?> getCommandClassFromHandler(Class<?> handlerClass, Class<?> targetInterface) {
        Type[] genericInterfaces = handlerClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                if (rawType.equals(targetInterface)) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                        return (Class<?>) typeArguments[0];
                    }
                }
            }
        }
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
