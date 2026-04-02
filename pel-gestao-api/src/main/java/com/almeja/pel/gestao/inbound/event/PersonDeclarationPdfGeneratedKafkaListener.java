package com.almeja.pel.gestao.inbound.event;

import com.almeja.pel.gestao.core.event.dto.EventPersonDeclarationPdfGeneratedDTO;
import com.almeja.pel.gestao.core.memory.PersonDeclarationMemory;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PersonDeclarationPdfGeneratedKafkaListener {

    @KafkaListener(topics = "${spring.kafka.topics.person-declaration-pdf-generated}", groupId = "${spring.kafka.consumer.group-id}")
    public void execute(ConsumerRecord<String, String> record) {
        EventPersonDeclarationPdfGeneratedDTO declarationPdfGenerated = new Gson().fromJson(record.value(), EventPersonDeclarationPdfGeneratedDTO.class);
        log.info("PDF de declaracao gerado para cpf {}", declarationPdfGenerated.getCpf());
        PersonDeclarationMemory.getInstance().saveDeclaration(declarationPdfGenerated.getCpf(), declarationPdfGenerated.getDeclarationPdf());
    }

}
