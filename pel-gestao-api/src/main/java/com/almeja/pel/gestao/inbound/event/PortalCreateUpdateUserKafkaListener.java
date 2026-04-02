package com.almeja.pel.gestao.inbound.event;

import com.almeja.pel.gestao.core.domain.usecase.person.CreateUpdatePersonFromPortalUserUC;
import com.almeja.pel.gestao.core.dto.PortalUserDTO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PortalCreateUpdateUserKafkaListener {

    private final CreateUpdatePersonFromPortalUserUC createUpdatePersonFromPortalUserUC;

    @KafkaListener(topics = "${spring.kafka.topics.portal-update-create-user}", groupId = "${spring.kafka.consumer.group-id}")
    public void execute(ConsumerRecord<String, String> record) {
        PortalUserDTO portalUser = new Gson().fromJson(record.value(), PortalUserDTO.class);
        log.info("Usuário portal com cpf {} recebido para atualização ou cadastro", portalUser.getCpf());
        createUpdatePersonFromPortalUserUC.execute(portalUser);
    }

}
