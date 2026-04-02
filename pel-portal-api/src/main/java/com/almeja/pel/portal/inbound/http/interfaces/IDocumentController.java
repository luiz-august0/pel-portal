package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IDocumentController.PATH)
public interface IDocumentController {

    String PATH = PREFIX_PATH + "/document";

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    void upload(@RequestParam(name = "documentType") EnumDocumentType documentType, @RequestBody MultipartDTO multipartDTO);

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    void delete(@RequestParam(name = "documentType") EnumDocumentType documentType);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    DocumentDTO getDocument(@RequestParam(name = "documentType") EnumDocumentType documentType);

    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    byte[] downloadDocument(@RequestParam(name = "documentType") EnumDocumentType documentType);

}
