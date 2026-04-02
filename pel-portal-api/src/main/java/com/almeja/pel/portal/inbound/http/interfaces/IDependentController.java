package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.*;
import com.almeja.pel.portal.infra.dto.DependentDTO;
import com.almeja.pel.portal.infra.dto.DependentsLinkedListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IDependentController.PATH)
public interface IDependentController {

    String PATH = PREFIX_PATH + "/dependent";

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    UUID create(@RequestBody DependentCreateDTO dependentCreateDTO);

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    DependentsLinkedListDTO getList();

    @GetMapping("/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    DependentDTO getInfo(@PathVariable("id") UUID id);

    @PostMapping("/{id}/recognize")
    @ResponseStatus(HttpStatus.OK)
    void recognize(@PathVariable("id") UUID id,
                   @RequestParam(name = "recognize", defaultValue = "false") boolean recognize);

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    void updateDependent(@PathVariable("id") UUID id,
                         @RequestBody UserUpdateDTO userUpdateDTO);

    @PutMapping("/{id}/update-relationship-special-needs")
    @ResponseStatus(HttpStatus.OK)
    void addRelationshipAndUpdateSpecialNeeds(@PathVariable("id") UUID id,
                                              @RequestBody DependentRelationshipAndSpecialNeedsDTO dto);

    @PostMapping("/{id}/address")
    @ResponseStatus(HttpStatus.OK)
    void createUpdateAddress(@PathVariable("id") UUID id,
                             @RequestBody CreateUpdateDependentAddressDTO dto);

    @PostMapping("/{id}/document/upload")
    @ResponseStatus(HttpStatus.CREATED)
    void uploadDocument(@PathVariable("id") UUID id,
                        @RequestParam(name = "documentType") EnumDocumentType documentType,
                        @RequestBody MultipartDTO multipartDTO);

    @GetMapping("/{id}/document")
    @ResponseStatus(HttpStatus.OK)
    DocumentDTO getDocument(@PathVariable("id") UUID id,
                            @RequestParam(name = "documentType") EnumDocumentType documentType);

    @GetMapping("/{id}/document/download")
    @ResponseStatus(HttpStatus.OK)
    byte[] downloadDocument(@PathVariable("id") UUID id,
                            @RequestParam(name = "documentType") EnumDocumentType documentType);

    @DeleteMapping("/{id}/document/delete")
    @ResponseStatus(HttpStatus.OK)
    void deleteDocument(@PathVariable("id") UUID id,
                        @RequestParam(name = "documentType") EnumDocumentType documentType);

    @GetMapping("/responsible")
    DependentDTO getResponsible();

}
