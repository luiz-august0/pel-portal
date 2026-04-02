package com.almeja.pel.gestao.core.event;

import com.almeja.pel.gestao.core.gateway.event.EventProducerGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AskPersonDeclarationPdfEvent {

    @Value("${spring.kafka.topics.person-declaration-pdf-ask}")
    private String eventName;

    private final EventProducerGTW eventProducerGTW;

    public void send(String cpf) {
        eventProducerGTW.send(eventName, cpf);
    }

}
