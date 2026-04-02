package com.almeja.pel.gestao.core.gateway.event;

public interface EventProducerGTW {

    void send(String eventName, Object object);

}
