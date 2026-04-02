package com.almeja.pel.gestao.infra.util;

import com.almeja.pel.gestao.core.dto.MultipartDTO;
import com.almeja.pel.gestao.core.exception.AppException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class FileUtil {

    public static File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }

        return convertedFile;
    }

    public static File convertMultipartBeanToFile(MultipartDTO multipartDTO) {
        File convertedFile = new File(Objects.requireNonNull(multipartDTO.getFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartDTO.getBytes());
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }

        return convertedFile;
    }

    public static File convertBytesToFile(byte[] fileBytes, String filename) {
        File convertedFile = new File(Objects.requireNonNull(filename));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(fileBytes);
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }

        return convertedFile;
    }

    public static String convertBytesToBase64(byte[] fileBytes) {
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    public static String generateUniqueFilename(String filename) {
        String extension = "";
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = filename.substring(lastDotIndex);
        }
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
    }

    public static String getFilenameFromS3Url(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}