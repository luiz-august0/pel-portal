package com.almeja.pel.gestao.core.mediator.command;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.InscriptionGroupedByYearDTO;
import com.almeja.pel.gestao.core.mediator.Command;

import java.util.List;

public record GetInscriptionsGroupedByYearCommand(
        PersonEntity person
) implements Command<List<InscriptionGroupedByYearDTO>> {
}
