package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.DocumentValidatorService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "portal_document")
public class DocumentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "document_type", nullable = false)
    private EnumDocumentType documentType;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "size", nullable = false)
    private BigDecimal size;

    @Column(name = "s3_file", nullable = false)
    private Boolean s3File;

    public DocumentEntity(UserEntity user, EnumDocumentType documentType, String originalFilename, String filename, BigDecimal size, Boolean s3File) {
        DocumentValidatorService.validate(user, documentType, filename, originalFilename, size);
        this.user = user;
        this.documentType = documentType;
        this.originalFilename = originalFilename;
        this.filename = filename.trim();
        this.size = size;
        this.s3File = s3File;
    }

    @Override
    public void prePersist() {
        super.prePersist();
        this.s3File = Boolean.TRUE.equals(this.s3File);
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        this.s3File = Boolean.TRUE.equals(this.s3File);
    }

}
