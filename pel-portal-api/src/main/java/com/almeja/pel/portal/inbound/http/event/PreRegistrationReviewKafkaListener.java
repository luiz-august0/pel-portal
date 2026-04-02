package com.almeja.pel.portal.inbound.http.event;

import com.almeja.pel.portal.core.domain.usecase.user.SubmitUserReviewUC;
import com.almeja.pel.portal.core.event.dto.EventPreRegistrationReviewDTO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PreRegistrationReviewKafkaListener {

    private final SubmitUserReviewUC submitUserReviewUC;

    @KafkaListener(topics = "${spring.kafka.topics.pre-registration-review}", groupId = "${spring.kafka.consumer.group-id}")
    public void execute(ConsumerRecord<String, String> record) {
        EventPreRegistrationReviewDTO eventPreRegistrationReviewDTO = new Gson().fromJson(record.value(), EventPreRegistrationReviewDTO.class);
        log.info("Pré cadastro com cpf {} aprovado", eventPreRegistrationReviewDTO.getCpf());
        submitUserReviewUC.execute(eventPreRegistrationReviewDTO.getCpf().replaceAll("[^0-9]", ""), eventPreRegistrationReviewDTO.getApproved());
    }

}
