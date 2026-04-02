package com.almeja.pel.gestao.inbound.event;

import com.almeja.pel.gestao.core.domain.usecase.inscription.SaveInscriptionContractPdfUC;
import com.almeja.pel.gestao.core.event.dto.EventInscriptionContractPdfGeneratedDTO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InscriptionContractPdfGeneratedKafkaListener {

    private final SaveInscriptionContractPdfUC saveInscriptionContractPdfUC;

    @KafkaListener(topics = "${spring.kafka.topics.inscription-contract-pdf-generated}", groupId = "${spring.kafka.consumer.group-id}")
    public void execute(ConsumerRecord<String, String> record) {
        EventInscriptionContractPdfGeneratedDTO contractPdfGenerated = new Gson().fromJson(record.value(), EventInscriptionContractPdfGeneratedDTO.class);
        log.info("PDF de contrato gerado da inscrição {}", contractPdfGenerated.getInscriptionId());
        saveInscriptionContractPdfUC.execute(contractPdfGenerated.getInscriptionId(), contractPdfGenerated.getContractPdf());
    }

}
