package com.almeja.pel.portal.infra.service.event;

import com.almeja.pel.portal.core.gateway.event.EventProducerGTW;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Channel("kafka-producer")
    Emitter<String> emitter;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public void send(String topicName, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);

            OutgoingKafkaRecordMetadata<String> metadata =
                    OutgoingKafkaRecordMetadata.<String>builder()
                            .withTopic(topicName)
                            .build();

            emitter.send(
                    Message.of(json).addMetadata(metadata)
            );

            log.info("Mensagem enviada para o tópico '{}': {}", topicName, json);

        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o tópico '{}': {}", topicName, payload, e);
        }
    }

}
