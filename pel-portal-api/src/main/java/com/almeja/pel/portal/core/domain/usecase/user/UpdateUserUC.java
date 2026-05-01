package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.factory.UserFactory;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateUserUC {

    @Inject
    UserRepository userRepository;
    @Inject
    UserFactory userFactory;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserEntity user, UserUpdateDTO userUpdateDTO) {
        // Usar UserFactory para atualizar com validações
        userFactory.update(user, userUpdateDTO.getName(), userUpdateDTO.getEmail(), userUpdateDTO.getCpf(),
                userUpdateDTO.getBirthDate(), userUpdateDTO.getPhone());
        // Salvar usuário atualizado
        userRepository.save(user);
        mediator.send(new UpdateUserCommand(user));
    }
}
