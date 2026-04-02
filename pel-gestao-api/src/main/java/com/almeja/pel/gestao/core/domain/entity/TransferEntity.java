package com.almeja.pel.gestao.core.domain.entity;

import com.almeja.pel.gestao.core.domain.entity.base.BaseEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumTransferStatus;
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
@Table(name = "transferencia")
public class TransferEntity extends BaseEntity {

    @Id
    @Column(name = "id_transferencia")
    @SequenceGenerator(name = "id_transferencia", sequenceName = "seq_transferencia", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_transferencia")
    private Integer id;

    @Column(name = "dt_solicitacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matricula_origem", nullable = false)
    private InscriptionEntity sourceInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma_destino", nullable = false)
    private ClassEntity destinationClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matricula_destino")
    private InscriptionEntity destinationInscription;

    @Column(name = "dt_aprovacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;

    @Column(name = "cs_situacao")
    private EnumTransferStatus status;

    @Column(name = "ds_observacao")
    private String observation;

    @Column(name = "ds_usuario_ultima_alteracao", length = 11)
    private String lastUpdateUser;

    @Column(name = "dh_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    public TransferEntity(InscriptionEntity sourceInscription, ClassEntity destinationClass) {
        this.sourceInscription = sourceInscription;
        this.destinationClass = destinationClass;
        this.requestDate = new Date();
        this.status = EnumTransferStatus.P;
    }

    private void onSave() {
        this.lastUpdateUser = "precadastro";
        this.lastUpdateDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        onSave();
        if (this.requestDate == null) {
            this.requestDate = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        onSave();
    }

}
