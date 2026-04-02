package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "endereco")
public class AddressEntity extends BaseEntity {

    @Id
    @Column(name = "id_endereco")
    @SequenceGenerator(name = "id_endereco", sequenceName = "seq_endereco", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_endereco")
    private Integer id;

    @Column(name = "ds_logradouro", length = 100)
    private String street;

    @Column(name = "ds_numero", length = 15)
    private String number;

    @Column(name = "ds_bairro", length = 60)
    private String neighborhood;

    @Column(name = "ds_complemento", length = 80)
    private String complement;

    @Column(name = "nr_cep", length = 10)
    private String zipCode;

    @Column(name = "ds_municipio", length = 80)
    private String city;

    @Column(name = "sg_uf", length = 2)
    private String state;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        onSave();
    }

    @PreUpdate
    public void preUpdate() {
        onSave();
    }

}
