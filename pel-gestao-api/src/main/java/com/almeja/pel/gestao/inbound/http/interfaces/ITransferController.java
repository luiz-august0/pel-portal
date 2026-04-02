package com.almeja.pel.gestao.inbound.http.interfaces;

import com.almeja.pel.gestao.infra.dto.TransferDTO;
import com.almeja.pel.gestao.infra.dto.TransferGroupedByYearDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.almeja.pel.gestao.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(ITransferController.PATH)
public interface ITransferController {

    String PATH = PREFIX_PATH + "/transfer";

    @PostMapping()
    Integer register(@RequestParam(name = "inscriptionId") Integer inscriptionId,
                     @RequestParam(name = "classId") Integer classId);

    @GetMapping("/grouped-by-year")
    List<TransferGroupedByYearDTO> getGroupedByYear();

    @GetMapping("/{id}/details")
    TransferDTO getDetails(@PathVariable("id") Integer id);

}
