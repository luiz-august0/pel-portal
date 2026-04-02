package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateInternalRelationshipTypeUC {

    private final UserRepositoryGTW userRepositoryGTW;
    private final Mediator mediator;

    @Transactional
    public void execute(UserEntity user, EnumInternalRelationshipType internalRelationshipType) {
        if (user.getUserDetails().isMinor())
            throw new ValidatorException("Usuário menor não pode atualizar o tipo de relacionamento interno");
        user.validateReviewed();
        user.getUserDetails().updateInternalRelationshipType(internalRelationshipType);
        user.setReviewed(false);
        userRepositoryGTW.save(user);
        mediator.send(new UpdateUserCommand(user));
    }

}
