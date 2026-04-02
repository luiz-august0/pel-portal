package com.almeja.pel.portal.core.gateway.file;

import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.dto.record.FileUploadedRecord;

public interface FileHandlerGTW {

    FileUploadedRecord uploadFile(MultipartDTO multipartDTO);

    void deleteFile(String filename, boolean fromS3);

    byte[] getFile(String filename, boolean fromS3);

}
