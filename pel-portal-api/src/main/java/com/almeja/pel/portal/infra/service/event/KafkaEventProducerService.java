package com.almeja.pel.portal.infra.service.event;

import com.almeja.pel.portal.core.gateway.event.EventProducerGTW;
import com.google.gson.Gson;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
@Slf4j
public class KafkaEventProducerService implements EventProducerGTW {

    @Inject
    @Channel("portal-update-create-user")
    Emitter<String> emitter;

    @Override
    public void send(String eventName, Object object) {
        String jsonObj = new Gson().toJson(object);
        try {
            emitter.send(Message.of(jsonObj)
                    .addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
                            .withTopic(eventName)
                            .build()));
            log.info("Mensagem enviada com sucesso para o topico {} com objeto: {}", eventName, jsonObj);
        } catch (Exception e) {
            log.error("Erro inesperado ao enviar mensagem Kafka para topico {} com objeto {}", eventName, jsonObj, e);
        }
    }

}
