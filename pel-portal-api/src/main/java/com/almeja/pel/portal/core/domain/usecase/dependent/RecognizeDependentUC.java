package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecognizeDependentUC {

    private final UserDependentRepositoryGTW userDependentRepositoryGTW;
    private final UserRepositoryGTW userRepositoryGTW;
    private final Mediator mediator;

    @Transactional
    public void execute(UUID dependentId, boolean recognize, UserEntity responsible) {
        UserEntity dependentUser = userRepositoryGTW.findById(dependentId).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        UserDependentEntity dependentLink = userDependentRepositoryGTW.findByUserAndDependent(responsible, dependentUser)
                .orElseThrow(() -> new AppException("Dependente não vinculado ao responsável"));
        // Se já autorizado, não faz nada
        if (dependentUser.getAuthorized()) return;
        // Se reconhecido menor é autorizado
        if (recognize) {
            dependentUser.setAuthorized(true);
            userRepositoryGTW.save(dependentUser);
            mediator.send(new UpdateUserCommand(dependentUser));
            return;
        }
        // Se não reconhecido menor continua desautorizado e vinculo é excluido
        userDependentRepositoryGTW.deleteById(dependentLink.getId());
    }

}
