package com.almeja.pel.portal.core.mail.builders;

import com.almeja.pel.portal.core.domain.entity.TemplateEmailEntity;
import com.almeja.pel.portal.core.domain.enums.EnumTemplateEmail;
import com.almeja.pel.portal.core.mail.MailBuilder;
import com.almeja.pel.portal.core.mail.interfaces.ITemplate;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RecoveryMailBuilder extends MailBuilder<String> {

    @ConfigProperty(name = "app.url")
    String url;

    public RecoveryMailBuilder() {
        super(EnumTemplateEmail.RECOVERY);
    }

    @Override
    public ITemplate build(String token) {
        TemplateEmailEntity templateEmail = getTemplateEmail();

        String html = templateEmail.getHtml().replaceAll("::url_redirect_signature::", url + "/login/recuperacao/" + token);

        return new ITemplate() {
            @Override
            public String getHtml() {
                return html;
            }

            @Override
            public String getSubject() {
                return templateEmail.getTemplateEmail().getSubject();
            }
        };
    }

}
