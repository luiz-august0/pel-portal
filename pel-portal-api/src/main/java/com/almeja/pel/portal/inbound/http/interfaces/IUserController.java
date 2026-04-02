package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.dto.UserDTO;
import com.almeja.pel.portal.core.dto.UserStatusDTO;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.dto.record.ChangePasswordRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IUserController.PATH)
public interface IUserController {

    String PATH = PREFIX_PATH + "/user";

    @GetMapping("/current")
    UserDTO getCurrentUser();

    @PostMapping("/regenerate-responsible-link")
    AuthorizedLinkGeneratedRecord regenerateResponsibleLink();

    @PutMapping("/update")
    void updateUser(@RequestBody UserUpdateDTO userUpdateDTO);

    @PatchMapping("/update-internal-relationship-type")
    void updateInternalRelationshipType(@RequestParam(name = "relationshipType", required = false) EnumInternalRelationshipType relationshipType);

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    UUID createUpdateAddress(@RequestBody CreateUpdateAddressDTO createUpdateAddressDTO);

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    List<UserStatusDTO> getUserStatus();

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    void changePassword(@RequestBody ChangePasswordRecord changePasswordRecord);

}
