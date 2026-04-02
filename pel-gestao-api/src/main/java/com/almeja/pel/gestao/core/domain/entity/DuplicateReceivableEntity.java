package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "duplicata_receber")
public class DuplicateReceivableEntity extends BaseEntity {

    @Id
    @Column(name = "id_duplicata")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_duplicata")
    @MapsId
    private DuplicateEntity duplicate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matricula")
    private InscriptionEntity inscription;

    @Column(name = "pe_juro_mora", nullable = false)
    private BigDecimal lateInterestPercentage;

    @Column(name = "pe_multa", nullable = false)
    private BigDecimal finePercentage;

    @Column(name = "nr_boleto", length = 10)
    private String bankSlipNumber;

    @Column(name = "id_retorno_boleto")
    private Integer bankSlipReturn;


    @Column(name = "aq_boleto")
    private byte[] bankSlipFile;

    @Column(name = "in_reemissao")
    private Boolean reissue = false;

    @Column(name = "in_cobranca")
    private Boolean collection = false;

}
