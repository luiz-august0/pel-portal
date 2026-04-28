package com.almeja.pel.portal.core.mail;

import com.almeja.pel.portal.core.domain.entity.TemplateEmailEntity;
import com.almeja.pel.portal.core.domain.enums.EnumTemplateEmail;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.gateway.repository.TemplateEmailRepositoryGTW;
import com.almeja.pel.portal.core.mail.interfaces.ITemplate;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public abstract class MailBuilder<Parameter> {

    @Inject
    private TemplateEmailRepositoryGTW templateEmailRepositoryGTW;

    private final EnumTemplateEmail enumTemplateEmail;

    protected MailBuilder(EnumTemplateEmail enumTemplateEmail) {
        this.enumTemplateEmail = enumTemplateEmail;
    }

    protected TemplateEmailEntity getTemplateEmail() {
        Optional<TemplateEmailEntity> optionalTemplateEmail = templateEmailRepositoryGTW.findFirstByTemplateEmail(enumTemplateEmail);

        if (optionalTemplateEmail.isPresent()) {
            return optionalTemplateEmail.get();
        } else {
            throw new AppException("Não foi possível encontrar o template: " + enumTemplateEmail.getValue());
        }
    }

    public abstract ITemplate build(Parameter parameter);

}
