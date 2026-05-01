package com.almeja.pel.portal.infra.service.file;

import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.dto.record.FileUploadedRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.infra.service.aws.s3.S3StorageService;
import com.almeja.pel.portal.infra.util.FileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
@Slf4j
public class FileHandlerService implements FileHandlerGTW {

    @ConfigProperty(name = "file.upload-dir")
    String uploadDir;

    @ConfigProperty(name = "file.s3-enabled")
    boolean s3Enabled;

    @ConfigProperty(name = "file.upload-size")
    String uploadSize;

    @Inject
    S3StorageService s3StorageService;

    @Override
    public FileUploadedRecord uploadFile(MultipartDTO multipartDTO) {
        validateFile(multipartDTO);
        BigDecimal fileSize = BigDecimal.valueOf(getFileSizeInBytes(multipartDTO));
        try {
            if (s3Enabled) {
                log.info("Upload via S3 habilitado - enviando arquivo para bucket");
                return new FileUploadedRecord(s3StorageService.upload(multipartDTO, false), fileSize, true);
            } else {
                log.info("Upload local habilitado - salvando arquivo em: {}", uploadDir);
                return new FileUploadedRecord(uploadToLocal(multipartDTO), fileSize, false);
            }
        } catch (Exception e) {
            log.error("Erro ao fazer upload do arquivo: {}", multipartDTO.getFilename(), e);
            throw new AppException("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String filename, boolean fromS3) {
        try {
            if (s3Enabled && fromS3) {
                log.info("Deletando arquivo no S3: {}", filename);
                s3StorageService.delete(filename);
            } else {
                log.info("Deletando arquivo localmente: {}", filename);
                deleteFromLocal(filename);
            }
        } catch (Exception e) {
            log.error("Erro ao deletar arquivo: {}", filename, e);
            throw new AppException("Erro ao deletar arquivo: " + e.getMessage());
        }
    }

    @Override
    public byte[] getFile(String filename, boolean fromS3) {
        log.info("Buscando arquivo: {}", filename);
        if (filename == null || filename.trim().isEmpty())
            throw new ValidatorException("Nome do arquivo é obrigatório");
        try {
            if (s3Enabled && fromS3) {
                log.info("Buscando arquivo no S3: {}", filename);
                return s3StorageService.download(filename);
            } else {
                log.info("Buscando arquivo localmente: {}", filename);
                return getFileFromLocal(filename);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar arquivo: {}", filename, e);
            throw new AppException("Erro ao buscar arquivo: " + e.getMessage());
        }
    }

    private String uploadToLocal(MultipartDTO multipartDTO) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Diretório de upload criado: {}", uploadDir);
            }
            String filename = FileUtil.generateUniqueFilename(multipartDTO.getFilename());
            Path filePath = uploadPath.resolve(filename);
            File file = filePath.toFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartDTO.getBytes());
            }
            String relativePath = uploadDir + "/" + filename;
            log.info("Arquivo salvo localmente: {}", relativePath);
            return filename;
        } catch (IOException e) {
            log.error("Erro ao salvar arquivo localmente: {}", multipartDTO.getFilename(), e);
            throw new AppException("Erro ao salvar arquivo localmente: " + e.getMessage());
        }
    }

    private void deleteFromLocal(String filename) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(filename);
            if (!Files.exists(filePath)) throw new AppException("Arquivo não encontrado: " + filename);
            Files.delete(filePath);
            log.info("Arquivo deletado localmente: {}", filename);
        } catch (IOException e) {
            log.error("Erro ao deletar arquivo local: {}", filename, e);
            throw new AppException("Erro ao deletar arquivo local: " + e.getMessage());
        }
    }

    private byte[] getFileFromLocal(String filename) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(filename);
            if (!Files.exists(filePath)) throw new AppException("Arquivo não encontrado: " + filename);
            byte[] fileBytes = Files.readAllBytes(filePath);
            log.info("Arquivo lido localmente com sucesso: {}", filename);
            return fileBytes;
        } catch (IOException e) {
            log.error("Erro ao ler arquivo local: {}", filename, e);
            throw new AppException("Erro ao ler arquivo local: " + e.getMessage());
        }
    }

    private void validateFile(MultipartDTO multipartDTO) {
        if (multipartDTO == null) {
            throw new ValidatorException("Arquivo é obrigatório");
        }
        if (multipartDTO.getFilename() == null || multipartDTO.getFilename().trim().isEmpty()) {
            throw new ValidatorException("Nome do arquivo é obrigatório");
        }
        if (multipartDTO.getFile() == null || multipartDTO.getFile().trim().isEmpty()) {
            throw new ValidatorException("Conteúdo do arquivo é obrigatório");
        }
        validateFileSize(multipartDTO);
        String filename = multipartDTO.getFilename().toLowerCase();
        if (!isValidFileExtension(filename)) {
            throw new ValidatorException("Tipo de arquivo não permitido");
        }
    }

    private void validateFileSize(MultipartDTO multipartDTO) {
        long fileSizeInBytes = getFileSizeInBytes(multipartDTO);
        long maxSizeInBytes = parseUploadSize();
        if (fileSizeInBytes > maxSizeInBytes) {
            String fileSizeFormatted = formatFileSize(fileSizeInBytes);
            throw new ValidatorException(String.format("Arquivo muito grande. Tamanho atual: %s. Tamanho máximo permitido: %s", fileSizeFormatted, uploadSize));
        }
        log.debug("Arquivo validado - Tamanho: {} bytes (máximo: {} bytes)", fileSizeInBytes, maxSizeInBytes);
    }

    private static long getFileSizeInBytes(MultipartDTO multipartDTO) {
        byte[] fileBytes = multipartDTO.getBytes();
        return fileBytes.length;
    }

    private long parseUploadSize() {
        if (uploadSize == null || uploadSize.trim().isEmpty()) {
            return 10 * 1024 * 1024;
        }
        String size = uploadSize.trim().toUpperCase();
        long multiplier = 1;
        if (size.endsWith("KB")) {
            multiplier = 1024;
            size = size.substring(0, size.length() - 2);
        } else if (size.endsWith("MB")) {
            multiplier = 1024 * 1024;
            size = size.substring(0, size.length() - 2);
        } else if (size.endsWith("GB")) {
            multiplier = 1024 * 1024 * 1024;
            size = size.substring(0, size.length() - 2);
        }
        try {
            return Long.parseLong(size.trim()) * multiplier;
        } catch (NumberFormatException e) {
            log.warn("Formato de tamanho de arquivo inválido: {}. Usando padrão de 10MB", uploadSize);
            return 10 * 1024 * 1024;
        }
    }

    private String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " bytes";
        } else if (sizeInBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeInBytes / 1024.0);
        } else if (sizeInBytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", sizeInBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private boolean isValidFileExtension(String filename) {
        String[] allowedExtensions = {".pdf", ".jpg", ".jpeg", ".png", ".doc", ".docx", ".txt"};
        for (String extension : allowedExtensions) {
            if (filename.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
