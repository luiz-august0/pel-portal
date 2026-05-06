package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RecognizeDependentUC {

    private final UserDependentRepository userDependentRepository;
    private final UserRepository userRepository;
    private final Mediator mediator;

    @Transactional
    public void execute(UUID dependentId, boolean recognize, UserEntity responsible) {
        UserEntity dependentUser = userRepository.findById(dependentId).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        UserDependentEntity dependentLink = userDependentRepository.findByUserAndDependent(responsible, dependentUser)
                .orElseThrow(() -> new AppException("Dependente não vinculado ao responsável"));
        // Se já autorizado, não faz nada
        if (dependentUser.getAuthorized()) return;
        // Se reconhecido menor é autorizado
        if (recognize) {
            dependentUser.setAuthorized(true);
            userRepository.save(dependentUser);
            mediator.send(new UpdateUserCommand(dependentUser));
            return;
        }
        // Se não reconhecido menor continua desautorizado e vinculo é excluido
        userDependentRepository.deleteById(dependentLink.getId());
    }

}
