package com.almeja.pel.gestao.core.domain.service;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetClassInfoService {

    private final ClassRepositoryGTW classRepositoryGTW;

    public ClassEntity getClassInfo(Integer classId) {
        ClassEntity classEntity = classRepositoryGTW.findAndValidate(classId);
        classEntity.setSubscribers(classRepositoryGTW.getSubscribersByClass(classId));
        return classEntity;
    }

}
