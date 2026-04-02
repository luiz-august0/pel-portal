package com.almeja.pel.gestao.core.domain.factory;

import com.almeja.pel.gestao.core.domain.entity.FileEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonFileEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumFileType;
import com.almeja.pel.gestao.core.gateway.file.FileHandlerGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileFactory {

    private final FileHandlerGTW fileHandlerGTW;
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpg", "image/jpg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("doc", "application/msword");
        mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put("xls", "application/vnd.ms-excel");
        mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
        mimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    public FileEntity createFile(String filename, boolean fromS3) {
        return FileEntity.builder()
                .fileName(filename)
                .mimeType(getMimeTypeFromFilename(filename))
                .fileData(fileHandlerGTW.getFile(filename, fromS3))
                .build();
    }

    public PersonFileEntity createPersonFile(FileEntity file, PersonEntity person, EnumFileType fileType) {
        return PersonFileEntity.builder()
                .file(file)
                .person(person)
                .fileType(fileType)
                .build();
    }

    private String getMimeTypeFromFilename(String filename) {
        return mimeTypes.get(filename.substring(filename.lastIndexOf(".") + 1));
    }

}
