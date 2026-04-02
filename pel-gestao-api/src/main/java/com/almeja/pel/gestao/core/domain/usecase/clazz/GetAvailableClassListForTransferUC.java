package com.almeja.pel.gestao.core.domain.usecase.clazz;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import com.almeja.pel.gestao.core.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAvailableClassListForTransferUC {

    private final ClassRepositoryGTW classRepositoryGTW;
    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    public List<IClassInfoDTO> execute(Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        ClassEntity clazz = inscription.getClazz();
        return classRepositoryGTW.findAllActiveForTransfer(clazz.getCourse().getId(), clazz.getLevel().getId(),
                clazz.getPublicEntity().getId(), clazz.getId(), DateUtil.getYear(clazz.getPlannedStartDate()));
    }

}
