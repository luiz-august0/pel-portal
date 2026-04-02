package com.almeja.pel.gestao.inbound.http;

import com.almeja.pel.gestao.core.domain.usecase.transfer.GetTransferUC;
import com.almeja.pel.gestao.core.domain.usecase.transfer.GetTransfersGroupedByYearUC;
import com.almeja.pel.gestao.core.domain.usecase.transfer.RegisterTransferUC;
import com.almeja.pel.gestao.inbound.http.interfaces.ITransferController;
import com.almeja.pel.gestao.infra.context.AuthContext;
import com.almeja.pel.gestao.infra.dto.TransferDTO;
import com.almeja.pel.gestao.infra.dto.TransferGroupedByYearDTO;
import com.almeja.pel.gestao.infra.dto.mapper.TransferGroupedByYearMapper;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TransferController implements ITransferController {

    private final GetTransfersGroupedByYearUC getTransfersGroupedByYearUC;
    private final GetTransferUC getTransferUC;
    private final RegisterTransferUC registerTransferUC;

    @Override
    public Integer register(Integer inscriptionId, Integer classId) {
        return registerTransferUC.execute(AuthContext.getUser(), inscriptionId, classId);
    }

    @Override
    public List<TransferGroupedByYearDTO> getGroupedByYear() {
        return TransferGroupedByYearMapper.toDTO(getTransfersGroupedByYearUC.execute(AuthContext.getUser()));
    }

    @Override
    public TransferDTO getDetails(Integer id) {
        return ConverterEntityToDTOUtil.convert(getTransferUC.execute(AuthContext.getUser(), id), TransferDTO.class);
    }

}

