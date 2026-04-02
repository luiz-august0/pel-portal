package com.almeja.pel.gestao.inbound.event;

import com.almeja.pel.gestao.core.domain.usecase.inscription.SaveInscriptionCertificatePdfUC;
import com.almeja.pel.gestao.core.event.dto.EventInscriptionCertificatePdfGeneratedDTO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InscriptionCertificatePdfGeneratedKafkaListener {

    private final SaveInscriptionCertificatePdfUC saveInscriptionCertificatePdfUC;

    @KafkaListener(topics = "${spring.kafka.topics.inscription-certificate-pdf-generated}", groupId = "${spring.kafka.consumer.group-id}")
    public void execute(ConsumerRecord<String, String> record) {
        EventInscriptionCertificatePdfGeneratedDTO certificatePdfGenerated = new Gson().fromJson(record.value(), EventInscriptionCertificatePdfGeneratedDTO.class);
        log.info("PDF do certificado gerado da inscrição {}", certificatePdfGenerated.getInscriptionId());
        saveInscriptionCertificatePdfUC.execute(certificatePdfGenerated.getInscriptionId(), certificatePdfGenerated.getCertificatePdf());
    }

}
