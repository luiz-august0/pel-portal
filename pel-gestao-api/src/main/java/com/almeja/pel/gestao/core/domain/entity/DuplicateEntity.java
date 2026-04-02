package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumDuplicateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "duplicata")
public class DuplicateEntity extends BaseEntity {

    @Id
    @Column(name = "id_duplicata")
    @SequenceGenerator(name = "id_duplicata", sequenceName = "seq_duplicata", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_duplicata")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa")
    private PersonEntity person;

    @Column(name = "dt_emissao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Column(name = "dt_vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "dt_operacao")
    @Temporal(TemporalType.DATE)
    private Date operationDate;

    @Column(name = "cs_situacao", nullable = false)
    private EnumDuplicateStatus status;

    @Column(name = "nr_parcela")
    private Integer installmentNumber;

    @Column(name = "nr_parcela_total")
    private Integer totalInstallments;

    @Column(name = "vl_duplicata", nullable = false)
    private BigDecimal duplicateAmount;

    @Column(name = "vl_desconto", nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "vl_juro", nullable = false)
    private BigDecimal interestAmount;

    @Column(name = "vl_operacao", nullable = false)
    private BigDecimal operationAmount;

    @Column(name = "tx_observacao", length = 100)
    private String observation;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso")
    private CourseEntity course;

    @Column(name = "id_conta")
    private Integer account;

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
