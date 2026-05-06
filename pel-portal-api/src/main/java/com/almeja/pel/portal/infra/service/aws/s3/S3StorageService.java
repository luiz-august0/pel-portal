package com.almeja.pel.portal.infra.service.aws.s3;

import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.infra.util.FileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

@ApplicationScoped
@RequiredArgsConstructor
public class S3StorageService {

    @ConfigProperty(name = "cloud.aws.s3.bucket-name")
    String bucketName;

    private final S3Client s3Client;

    public String upload(MultipartDTO multipartDTO, Boolean isPublic) {
        File fileObj = FileUtil.convertMultipartBeanToFile(multipartDTO);
        String filename = FileUtil.generateUniqueFilename(multipartDTO.getFilename());
        try {
            PutObjectRequest.Builder builder = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename);
            if (isPublic) {
                builder.acl(ObjectCannedACL.PUBLIC_READ);
            }
            s3Client.putObject(builder.build(), RequestBody.fromFile(fileObj));
            return filename;
        } finally {
            fileObj.delete();
        }
    }

    public byte[] download(String fileName) {
        try {
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build());
            return objectBytes.asByteArray();
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public void delete(String fileName) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build());
    }

}
