package com.almeja.pel.portal.infra.service.event;

import com.almeja.pel.portal.core.gateway.event.EventProducerGTW;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
@Slf4j
public class KafkaEventProducerService implements EventProducerGTW {

    private final Emitter<String> emitter;
    private final ObjectMapper objectMapper;

    public KafkaEventProducerService(@Channel("kafka-producer") Emitter<String> emitter,
                                     ObjectMapper objectMapper) {
        this.emitter = emitter;
        this.objectMapper = objectMapper;
    }

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
