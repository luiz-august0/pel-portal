package com.almeja.pel.gestao.core.domain.usecase.transfer;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.TransferGroupedByYearDTO;
import com.almeja.pel.gestao.core.gateway.repository.TransferRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTransfersGroupedByYearUC {

    private final TransferRepositoryGTW transferRepositoryGTW;

    public List<TransferGroupedByYearDTO> execute(PersonEntity person) {
        List<String> years = transferRepositoryGTW.findYearsByPerson(person.getId());
        List<TransferGroupedByYearDTO> transferGroupedByYear = new ArrayList<>();
        for (String year : years) {
            TransferGroupedByYearDTO transferGroupedByYearDTO = new TransferGroupedByYearDTO(year,
                    transferRepositoryGTW.findAllByPersonAndYear(person.getId(), year));
            transferGroupedByYear.add(transferGroupedByYearDTO);
        }
        return transferGroupedByYear;
    }

}
