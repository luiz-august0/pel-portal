package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.DependentsLinkedListDTO;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ListDependentsLinkedUC {

    private final UserDependentRepository userDependentRepository;

    public DependentsLinkedListDTO execute(UserEntity user) {
        List<UserDependentEntity> dependents = userDependentRepository.findAllByUser(user);
        List<UserDependentEntity> pending = dependents.stream()
                .filter(dependent -> !dependent.getDependent().getAuthorized())
                .collect(Collectors.toList());
        List<UserDependentEntity> active = dependents.stream()
                .filter(dependent -> dependent.getDependent().getAuthorized())
                .collect(Collectors.toList());
        return new DependentsLinkedListDTO(pending, active);
    }

}
