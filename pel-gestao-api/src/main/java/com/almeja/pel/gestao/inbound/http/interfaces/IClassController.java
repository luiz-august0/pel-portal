package com.almeja.pel.gestao.inbound.http.interfaces;

import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;
import com.almeja.pel.gestao.infra.dto.ClassDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.almeja.pel.gestao.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IClassController.PATH)
public interface IClassController {

    String PATH = PREFIX_PATH + "/class";

    @GetMapping("/list-available")
    List<IClassInfoDTO> getListAvailable(@RequestParam(name = "course") Integer course,
                                         @RequestParam(name = "level") Integer level);

    @GetMapping("/{id}/details")
    ClassDTO getDetails(@PathVariable("id") Integer id);

    @GetMapping("/available-for-transfer")
    List<IClassInfoDTO> getAvailableForTransfer(@RequestParam(name = "inscriptionId") Integer inscriptionId);

}
