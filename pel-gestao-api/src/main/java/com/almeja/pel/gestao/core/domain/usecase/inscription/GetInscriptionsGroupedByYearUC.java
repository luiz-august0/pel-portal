package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.InscriptionGroupedByYearDTO;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetInscriptionsGroupedByYearUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    public List<InscriptionGroupedByYearDTO> execute(PersonEntity person) {
        List<String> years = inscriptionRepositoryGTW.findYearsByPerson(person.getId());
        List<InscriptionGroupedByYearDTO> inscriptionGroupedByYear = new ArrayList<>();
        for (String year : years) {
            InscriptionGroupedByYearDTO inscriptionGroupedByYearDTO = new InscriptionGroupedByYearDTO(year,
                    inscriptionRepositoryGTW.findAllByPersonAndYear(person.getId(), year));
            inscriptionGroupedByYear.add(inscriptionGroupedByYearDTO);
        }
        return inscriptionGroupedByYear;
    }

}
