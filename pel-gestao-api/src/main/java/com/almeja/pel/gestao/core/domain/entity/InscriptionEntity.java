package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionResult;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionStatus;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionType;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.util.DateUtil;
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
@Table(name = "matricula")
public class InscriptionEntity extends BaseEntity {

    @Id
    @Column(name = "id_matricula")
    @SequenceGenerator(name = "id_matricula", sequenceName = "seq_matricula", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_matricula")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private PersonEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma", nullable = false)
    private ClassEntity clazz;

    @Column(name = "cs_situacao", nullable = false)
    private EnumInscriptionStatus status;

    @Column(name = "tx_observacao")
    private String observation;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    @Column(name = "pe_frequencia")
    private BigDecimal attendancePercentage;

    @Column(name = "vl_nota_final")
    private BigDecimal finalGrade;

    @Column(name = "cs_resultado")
    private EnumInscriptionResult result;

    @Column(name = "cd_certificado", length = 20)
    private String certificateCode;

    @Column(name = "dt_matricula")
    @Temporal(TemporalType.DATE)
    private Date inscriptionDate;

    @Column(name = "dt_saida")
    @Temporal(TemporalType.DATE)
    private Date exitDate;

    @Column(name = "tx_contrato")
    private String contractText;

    @Column(name = "in_fechado", nullable = false)
    private Boolean closed;

    @Column(name = "ds_termo_imagem", length = 40)
    private String imageTermDescription;

    @Column(name = "tp_matricula", nullable = false)
    private EnumInscriptionType inscriptionType;

    @Column(name = "nm_aq_contrato")
    private String fileContractName;

    @Column(name = "aq_contrato_s3")
    private Boolean fileContractS3;

    @Column(name = "nm_aq_certificado")
    private String fileCertificateName;

    @Column(name = "aq_certificado_s3")
    private Boolean fileCertificateS3;

    @Column(name = "inscricao_finalizada", nullable = false)
    private Boolean inscriptionFinalized;

    @Transient
    private EnumInscriptionPayment paymentForm;

    public static InscriptionEntity create(PersonEntity person, ClassEntity clazz) {
        return InscriptionEntity.builder()
                .student(person)
                .clazz(clazz)
                .status(EnumInscriptionStatus.P)
                .inscriptionType(EnumInscriptionType.N)
                .inscriptionDate(new Date())
                .lastUpdateDate(new Date())
                .lastUpdateUser(person.getCpf())
                .closed(false)
                .inscriptionFinalized(false)
                .build();
    }

    public String getImageTerm() {
        String imageTerm = "<b>TERMO DE AUTORIZAÇÃO DE USO DE IMAGEM</b><br/><br/>";
        imageTerm += "O contratante autoriza o uso da sua imagem, e no caso, seu(sua) filho(a) (aluno(a)) na idade de " + this.student.getAge() + " anos " +
                "em todo e qualquer material, sejam fotos ou documentos, para ser utilizada em campanhas promocionais e institucionais " +
                "do Programa de Ensino de Línguas (PEL), destinadas à divulgação ao público em geral. " +
                "Fica acordado que o Contratante autoriza o uso da imagem do aluno em todo e qualquer material, " +
                "referente às aulas remotas disponibilizadas online por ferramentas de vídeo conferência. " +
                "A presente autorização é concedida a título gratuito, abrangendo o uso da imagem acima mencionada em todo território " +
                "nacional e no exterior, das seguintes formas: (I) outdoor; (II) busdoor; (III) folhetos em geral " +
                "(encartes, mala direta, catálogo, etc.); (IV) folder de apresentação; (V) anúncios em revistas e jornais em geral; " +
                "(VI) site; (VII) cartazes; (VIII) backlight; (IX) mídia eletrônica (redes sociais, painéis, vídeos, televisão, cinema, " +
                "programa para rádio, entre outros). Por esta ser a expressão da sua vontade, o contratante declara que autoriza o uso " +
                "acima descrito sem que nada haja a ser reclamado a título de direitos conexos à sua imagem ou a qualquer outro. " +
                "O contratante leu e concorda com a presente autorização.";
        return imageTerm;
    }

    public void validateStatusPendent() {
        if (!this.status.equals(EnumInscriptionStatus.P))
            throw new ValidatorException("A matrícula deve estar pendente para realizar esta operação.");
    }

    public boolean isValidToTransfer() {
        return this.status.equals(EnumInscriptionStatus.A) && this.result == null && this.clazz.getPlannedEndDate().after(DateUtil.getDate());
    }

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
        if (this.closed == null) this.closed = false;
        if (this.inscriptionType == null) this.inscriptionType = EnumInscriptionType.N;
    }

    @PrePersist
    public void prePersist() {
        onSave();
        this.inscriptionFinalized = false;
    }

    @PreUpdate
    public void preUpdate() {
        onSave();
    }

}
