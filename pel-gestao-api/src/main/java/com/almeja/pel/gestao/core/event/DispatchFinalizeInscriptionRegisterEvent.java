package com.almeja.pel.gestao.core.event;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.event.dto.EventFinalizeInscriptionRegisterDTO;
import com.almeja.pel.gestao.core.gateway.event.EventProducerGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchFinalizeInscriptionRegisterEvent {

    @Value("${spring.kafka.topics.finalize-inscription-register}")
    private String eventName;

    private final EventProducerGTW eventProducerGTW;

    public void send(PersonEntity person, InscriptionEntity inscription) {
        eventProducerGTW.send(eventName, EventFinalizeInscriptionRegisterDTO.builder()
                .inscriptionId(inscription.getId())
                .paymentForm(inscription.getPaymentForm().getKey())
                .internalCommunity(person.isInternalCommunity())
                .build());
    }

}
