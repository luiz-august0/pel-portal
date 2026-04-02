package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.DependentsLinkedListDTO;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListDependentsLinkedUC {

    private final UserDependentRepositoryGTW userDependentRepositoryGTW;

    public DependentsLinkedListDTO execute(UserEntity user) {
        List<UserDependentEntity> dependents = userDependentRepositoryGTW.findAllByUser(user);
        List<UserDependentEntity> pending = dependents.stream()
                .filter(dependent -> !dependent.getDependent().getAuthorized())
                .collect(Collectors.toList());
        List<UserDependentEntity> active = dependents.stream()
                .filter(dependent -> dependent.getDependent().getAuthorized())
                .collect(Collectors.toList());
        return new DependentsLinkedListDTO(pending, active);
    }

}
