package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDependentRelationship;
import com.almeja.pel.portal.core.exception.ValidatorException;
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
@Table(name = "portal_user_dependent")
public class UserDependentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", nullable = false)
    private UserEntity dependent;

    @Column(name = "dependent_relationship")
    private EnumDependentRelationship dependentRelationship;

    @Column(name = "from_link")
    private Boolean fromLink;

    public UserDependentEntity(UserEntity user, UserEntity dependent, boolean fromLink) {
        this.user = user;
        this.dependent = dependent;
        this.fromLink = fromLink;
    }

    public void updateDependentRelationship(EnumDependentRelationship dependentRelationship) {
        if (dependentRelationship == null)
            throw new ValidatorException("Tipo de parentesco do dependente é obrigatório");
        this.dependentRelationship = dependentRelationship;
    }

    @Override
    public void prePersist() {
        super.prePersist();
        if (this.fromLink == null) this.fromLink = false;
    }

}
