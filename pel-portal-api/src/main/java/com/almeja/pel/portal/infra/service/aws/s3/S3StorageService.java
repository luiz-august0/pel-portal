package com.almeja.pel.portal.infra.service.aws.s3;

import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.infra.util.FileUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3StorageService {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String upload(MultipartDTO multipartDTO, Boolean isPublic) {
        File fileObj = FileUtil.convertMultipartBeanToFile(multipartDTO);
        String filename = FileUtil.generateUniqueFilename(multipartDTO.getFilename());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, fileObj);
            if (isPublic) putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjectRequest);
            return filename;
        } finally {
            fileObj.delete();
        }
    }

    public byte[] download(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    public void delete(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

}