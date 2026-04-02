package com.almeja.pel.gestao.core.domain.usecase.clazz;

import com.almeja.pel.gestao.core.domain.entity.InscriptionConfigEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionConfigRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAvailableClassListUC {

    private final ClassRepositoryGTW classRepositoryGTW;
    private final InscriptionConfigRepositoryGTW inscriptionConfigRepositoryGTW;

    public List<IClassInfoDTO> execute(PersonEntity person, Integer course, Integer level) {
        InscriptionConfigEntity config = inscriptionConfigRepositoryGTW.getConfig();
        return classRepositoryGTW.findAllActiveByCourseAndLevel(course, level, person.getAge(), config.getInscriptionStartDate());
    }

}
