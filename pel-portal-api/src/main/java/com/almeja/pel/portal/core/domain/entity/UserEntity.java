package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.util.Util;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "portal_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "password", nullable = false)
    @Setter
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "authorized", nullable = false)
    @Setter
    private Boolean authorized;

    @Column(name = "reviewed", nullable = false)
    @Setter
    private Boolean reviewed;

    @Column(name = "responsible_token")
    @Setter
    private String responsibleToken;

    @Column(name = "responsible_token_generated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date responsibleTokenGeneratedAt;

    @Column(name = "responsible_token_expires_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date responsibleTokenExpiresAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id", nullable = false, unique = true)
    private UserDetailsEntity userDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressEntity address;

    @Override
    public void prePersist() {
        super.prePersist();
        if (this.active == null) this.active = true;
        if (this.authorized == null) this.authorized = false;
        if (this.reviewed == null) this.reviewed = false;
    }

    public UserEntity(String name, String email, String cpf, String password, UserDetailsEntity userDetails) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.userDetails = userDetails;
    }

    public void updateBasicInfo(String name, String email, String cpf) {
        if ((!Util.nvl(this.name, "").equals(Util.nvl(name, ""))) ||
                (!Util.nvl(this.cpf, "").equals(Util.nvl(cpf, "")))) {
            this.reviewed = false;
        }
        this.name = name;
        this.email = email;
        this.cpf = cpf;
    }

    public void updateAddress(AddressEntity address) {
        this.address = address;
    }

    public void validateReviewed() {
        if (this.reviewed.equals(Boolean.TRUE)) {
            throw new ValidatorException("Informações do usuário já foram revisadas, não é possível realizar mais alterações");
        }
    }

}
