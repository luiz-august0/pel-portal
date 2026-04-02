package com.almeja.pel.gestao.infra.service.event;

import com.almeja.pel.gestao.core.gateway.event.EventProducerGTW;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProducerService implements EventProducerGTW {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String eventName, Object object) {
        String jsonObj = new Gson().toJson(object);
        try {
            kafkaTemplate.send(eventName, jsonObj)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Mensagem enviada com sucesso para o tópico {} com objeto: {}", eventName, jsonObj);
                        } else {
                            log.error("Erro ao enviar mensagem para o tópico {} com objeto {}", eventName, jsonObj, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Erro inesperado ao enviar mensagem Kafka para tópico {} com objeto {}", eventName, jsonObj, e);
        }
    }

}
