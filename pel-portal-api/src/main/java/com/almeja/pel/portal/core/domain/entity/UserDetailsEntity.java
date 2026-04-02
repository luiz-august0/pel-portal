package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.util.DateUtil;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "portal_user_details")
public class UserDetailsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "special_needs", nullable = false)
    private Boolean specialNeeds;

    @Column(name = "program_knowledge_source", nullable = false)
    private EnumProgramKnowledgeSource programKnowledgeSource;

    @Column(name = "program_knowledge_source_other")
    private String programKnowledgeSourceOther;

    @Column(name = "internal_relationship_type")
    private EnumInternalRelationshipType internalRelationshipType;

    @Transient
    private boolean isMinor;

    @PostLoad
    public void postLoad() {
        this.isMinor = calculateIsMinor();
    }

    private boolean calculateIsMinor() {
        if (this.birthDate == null) {
            return false;
        }
        return DateUtil.getAge(this.birthDate) < 18;
    }

    public UserDetailsEntity(Date birthDate, String phone, Boolean specialNeeds, EnumProgramKnowledgeSource programKnowledgeSource, String programKnowledgeSourceOther) {
        UserValidatorService.validateBirthDate(birthDate);
        UserValidatorService.validatePhone(phone);
        UserValidatorService.validateSpecialNeeds(specialNeeds);
        this.birthDate = birthDate;
        this.phone = phone;
        this.specialNeeds = specialNeeds;
        this.programKnowledgeSource = programKnowledgeSource;
        this.programKnowledgeSourceOther = programKnowledgeSourceOther;
        this.isMinor = calculateIsMinor();
    }

    public void updateKnowledgeSourceFromOtherUser(UserDetailsEntity other) {
        this.programKnowledgeSource = other.getProgramKnowledgeSource();
        this.programKnowledgeSourceOther = other.getProgramKnowledgeSourceOther();
    }

    public void updateInternalRelationshipType(EnumInternalRelationshipType internalRelationshipType) {
        if (internalRelationshipType == null)
            throw new ValidatorException("Tipo de relacionamento interno é obrigatório");
        this.internalRelationshipType = internalRelationshipType;
    }

    public void updateSpecialNeeds(Boolean specialNeeds) {
        this.specialNeeds = Boolean.TRUE.equals(specialNeeds);
    }

    public void updateBasicInfo(Date birthDate, String phone) {
        UserValidatorService.validateBirthDate(birthDate);
        UserValidatorService.validatePhone(phone);
        this.birthDate = birthDate;
        this.phone = phone;
        this.isMinor = calculateIsMinor();
    }

}
