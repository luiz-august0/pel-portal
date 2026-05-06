package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UpdateInternalRelationshipTypeUC {

    private final UserRepository userRepository;
    private final Mediator mediator;

    @Transactional
    public void execute(UserEntity user, EnumInternalRelationshipType internalRelationshipType) {
        if (user.getUserDetails().isMinor())
            throw new ValidatorException("Usuário menor não pode atualizar o tipo de relacionamento interno");
        user.validateReviewed();
        user.getUserDetails().updateInternalRelationshipType(internalRelationshipType);
        user.setReviewed(false);
        userRepository.save(user);
        mediator.send(new UpdateUserCommand(user));
    }

}
