package com.almeja.pel.gestao.core.domain.usecase.clazz;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.domain.service.GetClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetClassInfoUC {

    private final GetClassInfoService getClassInfoService;

    public ClassEntity execute(Integer classId) {
        return getClassInfoService.getClassInfo(classId);
    }

}
