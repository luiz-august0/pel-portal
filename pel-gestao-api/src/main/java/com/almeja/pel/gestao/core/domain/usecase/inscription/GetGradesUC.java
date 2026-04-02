package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.dto.GradeByQuarterDTO;
import com.almeja.pel.gestao.core.dto.interfaces.IGradeQuarterDTO;
import com.almeja.pel.gestao.core.gateway.repository.GradeRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetGradesUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final GradeRepositoryGTW gradeRepositoryGTW;

    public List<GradeByQuarterDTO> execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        List<GradeByQuarterDTO> grades = new ArrayList<>();
        List<IGradeQuarterDTO> gradeQuarters = gradeRepositoryGTW.findQuartersByInscription(inscriptionId);
        gradeQuarters.forEach(gradeQuarter -> {
            GradeByQuarterDTO gradeByQuarterDTO = new GradeByQuarterDTO(gradeQuarter.getQuarter(), gradeQuarter.getGrade(),
                    gradeRepositoryGTW.findAllByInscriptionAndQuarter(inscriptionId, gradeQuarter.getQuarter()));
            grades.add(gradeByQuarterDTO);
        });
        return grades;
    }

}
