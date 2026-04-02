package com.almeja.pel.gestao.core.gateway.file;

import com.almeja.pel.gestao.core.dto.MultipartDTO;
import com.almeja.pel.gestao.core.dto.record.FileUploadedRecord;

public interface FileHandlerGTW {

    FileUploadedRecord uploadFile(MultipartDTO multipartDTO);

    void deleteFile(String filename, boolean fromS3);

    byte[] getFile(String filename, boolean fromS3);

    FileUploadedRecord uploadFile(byte[] fileBytes, String filename);

}
