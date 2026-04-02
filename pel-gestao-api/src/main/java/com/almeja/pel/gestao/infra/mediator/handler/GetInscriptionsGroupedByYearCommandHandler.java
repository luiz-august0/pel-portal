package com.almeja.pel.gestao.infra.mediator.handler;

import com.almeja.pel.gestao.core.domain.usecase.inscription.GetInscriptionsGroupedByYearUC;
import com.almeja.pel.gestao.core.dto.InscriptionGroupedByYearDTO;
import com.almeja.pel.gestao.core.mediator.CommandHandler;
import com.almeja.pel.gestao.core.mediator.command.GetInscriptionsGroupedByYearCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInscriptionsGroupedByYearCommandHandler implements CommandHandler<GetInscriptionsGroupedByYearCommand, List<InscriptionGroupedByYearDTO>> {

    private final GetInscriptionsGroupedByYearUC getInscriptionsGroupedByYearUC;

    @Override
    public List<InscriptionGroupedByYearDTO> handle(GetInscriptionsGroupedByYearCommand command) {
        return getInscriptionsGroupedByYearUC.execute(command.person());
    }

}
