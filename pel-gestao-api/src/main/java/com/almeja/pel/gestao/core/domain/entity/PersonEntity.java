package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import com.almeja.pel.gestao.core.util.DateUtil;
import com.almeja.pel.gestao.core.util.Util;
import com.almeja.pel.gestao.infra.util.FileUtil;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "pessoa")
public class PersonEntity extends BaseEntity {

    @Id
    @Column(name = "id_pessoa")
    @SequenceGenerator(name = "id_pessoa", sequenceName = "seq_pessoa", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pessoa")
    private Integer id;

    @Column(name = "nr_cpf", length = 20)
    private String cpf;

    @Column(name = "cpf_responsavel", length = 20)
    private String responsibleCpf;

    @Column(name = "nr_rg", length = 20)
    private String rg;

    @Column(name = "nm_pessoa", length = 60, nullable = false)
    private String name;

    @Column(name = "ds_senha", length = 128)
    private String password;

    @Column(name = "dt_nascimento")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco")
    private AddressEntity address;

    @Column(name = "ds_email", length = 60)
    private String email;

    @Column(name = "nr_telefone", length = 20)
    private String phone;

    @Column(name = "nr_celular", length = 20)
    private String cellphone;

    @Column(name = "lk_facebook", length = 80)
    private String facebookLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private PersonEntity responsible;

    @Column(name = "tp_vinculo", length = 3)
    private EnumBondType bondType;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "in_precadastro")
    private Boolean preRegistration;

    @Column(name = "in_ativo")
    private Boolean active;

    @Column(name = "cd_validacao", length = 40)
    private String validationCode;

    @Column(name = "nm_social", length = 60)
    private String socialName;

    @Column(name = "ds_naturalidade", length = 100)
    private String birthPlace;

    @Column(name = "ds_nacionalidade", length = 100)
    private String nationality;

    @Column(name = "cs_sexo", length = 1)
    private String gender;

    @Column(name = "nm_origem")
    private String origin;

    @Column(name = "dh_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<PersonFileEntity> personFiles;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        this.registrationDate = new Date();
        onSave();
    }

    @PreUpdate
    public void preUpdate() {
        onSave();
    }

    public boolean isNotReviewed() {
        return this.preRegistration.equals(Boolean.TRUE);
    }

    public Integer getAge() {
        return DateUtil.getAge(this.birthDate);
    }

    public boolean isMinor() {
        return DateUtil.getAge(this.birthDate) < 18;
    }

    public boolean isInternalCommunity() {
        return !this.getBondType().equals(EnumBondType.E);
    }

    public boolean updateNeedsReview(PersonEntity personEdited, List<PersonFileEntity> newPersonFiles) {
        if (!Util.nvl(this.cpf, "").equals(Util.nvl(personEdited.getCpf(), ""))) return true;
        if (!Util.nvl(this.name, "").equals(Util.nvl(personEdited.getName(), ""))) return true;
        if (!Util.nvl(this.birthDate, new Date()).equals(Util.nvl(personEdited.getBirthDate(), new Date())))
            return true;
        if (!Util.nvl(this.rg, "").equals(Util.nvl(personEdited.getRg(), ""))) return true;
        if (!Util.nvl(this.phone, "").equals(Util.nvl(personEdited.getPhone(), ""))) return true;
        if (!Util.nvl(this.responsibleCpf, "").equals(Util.nvl(personEdited.getResponsibleCpf(), ""))) return true;
        if (!Util.nvl(this.bondType, EnumBondType.E).equals(Util.nvl(personEdited.getBondType(), EnumBondType.E)))
            return true;
        if (!Util.nvl(this.birthPlace, "").equals(Util.nvl(personEdited.getBirthPlace(), ""))) return true;
        if (!Util.nvl(this.nationality, "").equals(Util.nvl(personEdited.getNationality(), ""))) return true;
        if (!Util.nvl(this.gender, "").equals(Util.nvl(personEdited.getGender(), ""))) return true;
        return newPersonFiles != null && newPersonFiles.stream().anyMatch(personFile -> {
            PersonFileEntity existedFile = this.personFiles.stream()
                    .filter((file) -> file.getFileType().equals(personFile.getFileType()))
                    .findFirst().orElse(null);
            if (existedFile == null) return true;
            String existedFileBase64 = FileUtil.convertBytesToBase64(existedFile.getFile().getFileData());
            String newFileBase64 = FileUtil.convertBytesToBase64(personFile.getFile().getFileData());
            return !existedFileBase64.equals(newFileBase64);
        });
    }

}
