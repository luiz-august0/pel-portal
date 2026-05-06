package com.almeja.pel.portal.inbound.event;

import com.almeja.pel.portal.core.domain.usecase.user.SubmitUserReviewUC;
import com.almeja.pel.portal.core.event.dto.EventPreRegistrationReviewDTO;
import com.google.gson.Gson;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PreRegistrationReviewKafkaListener {

    private final SubmitUserReviewUC submitUserReviewUC;

    @Incoming("pre-registration-review")
    public void execute(String payload) {
        EventPreRegistrationReviewDTO eventPreRegistrationReviewDTO = new Gson().fromJson(payload, EventPreRegistrationReviewDTO.class);
        log.info("Pre cadastro com cpf {} aprovado", eventPreRegistrationReviewDTO.getCpf());
        submitUserReviewUC.execute(eventPreRegistrationReviewDTO.getCpf().replaceAll("[^0-9]", ""), eventPreRegistrationReviewDTO.getApproved());
    }

}
