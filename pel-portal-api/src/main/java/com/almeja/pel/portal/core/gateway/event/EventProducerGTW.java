package com.almeja.pel.portal.core.gateway.event;

public interface EventProducerGTW {

    void send(String eventName, Object object);

}
