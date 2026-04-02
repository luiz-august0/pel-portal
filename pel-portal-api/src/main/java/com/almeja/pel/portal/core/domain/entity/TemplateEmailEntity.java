package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.enums.EnumTemplateEmail;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "portal_template_email")
public class TemplateEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "template_email", nullable = false, unique = true)
    private EnumTemplateEmail templateEmail;

    @Column(name = "html", nullable = false)
    private String html;

}