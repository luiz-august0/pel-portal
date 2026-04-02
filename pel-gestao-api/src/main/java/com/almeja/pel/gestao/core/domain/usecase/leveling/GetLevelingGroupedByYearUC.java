package com.almeja.pel.gestao.core.domain.usecase.leveling;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.LevelingGroupedByYearDTO;
import com.almeja.pel.gestao.core.gateway.repository.LevelingRegistrationRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLevelingGroupedByYearUC {

    private final LevelingRegistrationRepositoryGTW levelingRegistrationRepositoryGTW;

    public List<LevelingGroupedByYearDTO> execute(PersonEntity person) {
        List<String> years = levelingRegistrationRepositoryGTW.findYearsByPerson(person.getId());
        List<LevelingGroupedByYearDTO> levelingGroupedByYear = new ArrayList<>();
        for (String year : years) {
            LevelingGroupedByYearDTO levelingGroupedByYearDTO = new LevelingGroupedByYearDTO(year,
                    levelingRegistrationRepositoryGTW.findAllByPersonAndYear(person.getId(), year));
            levelingGroupedByYear.add(levelingGroupedByYearDTO);
        }
        return levelingGroupedByYear;
    }

}
