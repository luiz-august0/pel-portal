package com.almeja.pel.gestao.core.domain.usecase.person;

import com.almeja.pel.gestao.core.domain.entity.AddressEntity;
import com.almeja.pel.gestao.core.domain.entity.FileEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonFileEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import com.almeja.pel.gestao.core.domain.enums.EnumFileType;
import com.almeja.pel.gestao.core.domain.factory.AddressFactory;
import com.almeja.pel.gestao.core.domain.factory.FileFactory;
import com.almeja.pel.gestao.core.domain.factory.PersonFactory;
import com.almeja.pel.gestao.core.dto.PortalAddressDTO;
import com.almeja.pel.gestao.core.dto.PortalDocumentDTO;
import com.almeja.pel.gestao.core.dto.PortalUserDTO;
import com.almeja.pel.gestao.core.dto.enums.EnumPortalDocumentType;
import com.almeja.pel.gestao.core.gateway.repository.AddressRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.FileRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.PersonFileRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.PersonRepositoryGTW;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUpdatePersonFromPortalUserUC {

    private final PersonRepositoryGTW personRepositoryGTW;
    private final AddressRepositoryGTW addressRepositoryGTW;
    private final FileRepositoryGTW fileRepositoryGTW;
    private final PersonFileRepositoryGTW personFileRepositoryGTW;
    private final PersonFactory personFactory;
    private final AddressFactory addressFactory;
    private final FileFactory fileFactory;

    @Transactional
    public void execute(PortalUserDTO portalUserDTO) {
        try {
            PersonEntity person = createOrUpdatePerson(portalUserDTO);
            log.info("Pessoa {} salva com sucesso", person.getCpf());
        } catch (Exception e) {
            log.error("Erro ao criar ou atualizar pessoa", e);
        }
    }

    private PersonEntity createOrUpdatePerson(PortalUserDTO portalUserDTO) {
        Optional<PersonEntity> personOptional = personRepositoryGTW.findByCpf(portalUserDTO.getCpf());
        PersonEntity person = buildPerson(portalUserDTO);
        AddressEntity address = buildAddress(portalUserDTO.getAddress());
        List<PersonFileEntity> newPersonFiles = new ArrayList<>();
        // Arquivos da pessoa
        portalUserDTO.getDocuments().forEach(doc -> {
            newPersonFiles.add(buildPersonFile(saveFile(doc), null, doc.getDocumentType()));
        });
        // Se tiver responsavel deve salvar o documento do responsavel na pessoa
        if (portalUserDTO.getResponsible() != null) {
            portalUserDTO.getResponsible().getDocuments().stream()
                    .filter((doc) -> doc.getDocumentType().equals(EnumPortalDocumentType.DOCUMENT_WITH_PHOTO))
                    .findFirst().ifPresent((doc) -> {
                        FileEntity file = saveFile(doc);
                        newPersonFiles.add(fileFactory.createPersonFile(file, null, EnumFileType.RES));
                    });
        }
        // Atualiza a pessoa se ja tiver cadastro
        if (personOptional.isPresent()) {
            PersonEntity personSaved = personOptional.get();
            // Verifica se precisa de revisao
            person.setPreRegistration(personSaved.updateNeedsReview(person, newPersonFiles));
            // Deleta todos os arquivos da pessoa
            List<PersonFileEntity> personFiles = personFileRepositoryGTW.findAllByPerson(personSaved);
            personFiles.forEach(personFile -> {
                FileEntity file = personFile.getFile();
                personFileRepositoryGTW.delete(personFile);
                fileRepositoryGTW.delete(file);
            });
            person.setId(personSaved.getId());
            address.setId(personSaved.getAddress().getId());
            // Campos que nao devem ser atualizados
            person.setPassword(personSaved.getPassword());
            person.setRegistrationDate(personSaved.getRegistrationDate());
        }
        // Se tiver responsavel deve salvar informacoes do responsavel no usuario menor
        if (portalUserDTO.getResponsible() != null) {
            person.setPhone(portalUserDTO.getResponsible().getUserDetails().getPhone());
            person.setResponsibleCpf(portalUserDTO.getResponsible().getCpf());
            Optional<PersonEntity> responsible = personRepositoryGTW.findByCpf(portalUserDTO.getResponsible().getCpf());
            responsible.ifPresent(person::setResponsible);
        }
        addressRepositoryGTW.save(address);
        person.setAddress(address);
        PersonEntity personSaved = personRepositoryGTW.save(person);
        // Salva os arquivos da pessoa
        newPersonFiles.forEach(doc -> {
            doc.setPerson(personSaved);
            personFileRepositoryGTW.save(doc);
        });
        return personSaved;
    }

    private FileEntity saveFile(PortalDocumentDTO doc) {
        FileEntity file = buildFile(doc);
        return fileRepositoryGTW.save(file);
    }

    private PersonEntity buildPerson(PortalUserDTO portalUser) {
        return personFactory.createPerson(portalUser.getCpf(), portalUser.getName(), portalUser.getUserDetails().getBirthDate(),
                portalUser.getEmail(), portalUser.getUserDetails().getPhone(), portalUser.getUserDetails().getProgramKnowledgeSource().getValue(),
                portalUser.getUserDetails().getInternalRelationshipType() != null ? portalUser.getUserDetails().getInternalRelationshipType().getBondType() : EnumBondType.E,
                true);
    }

    private AddressEntity buildAddress(PortalAddressDTO address) {
        return addressFactory.createAddress(address.getStreet(), address.getNumber(), address.getNeighborhood(),
                address.getCity(), address.getState(), address.getCep(), address.getComplement());
    }

    private FileEntity buildFile(PortalDocumentDTO document) {
        return fileFactory.createFile(document.getFilename(), document.getS3File());
    }

    private PersonFileEntity buildPersonFile(FileEntity file, PersonEntity person, EnumPortalDocumentType documentType) {
        return fileFactory.createPersonFile(file, person, documentType.getFileType());
    }

}
