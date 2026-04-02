package com.almeja.pel.gestao.inbound.job;

import com.almeja.pel.gestao.core.domain.usecase.inscription.CancelPendingInscriptionsWithoutDuplicatesUC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class CancelPendingInscriptionsWithoutDuplicatesJob {

    private final CancelPendingInscriptionsWithoutDuplicatesUC cancelPendingInscriptionsWithoutDuplicatesUC;

    @Scheduled(cron = "0 0 * * * *")
    public void execute() {
        log.info("Cancelando inscrições pendentes sem duplicatas");
        cancelPendingInscriptionsWithoutDuplicatesUC.execute();
    }

}
