package com.almeja.pel.portal.infra.util;

import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.exception.AppException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class FileUtil {

    public static File convertMultipartBeanToFile(MultipartDTO multipartDTO) {
        File convertedFile = new File(Objects.requireNonNull(multipartDTO.getFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartDTO.getBytes());
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }

        return convertedFile;
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
