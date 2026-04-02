package com.almeja.pel.gestao.inbound.http;

import com.almeja.pel.gestao.core.domain.usecase.clazz.GetAvailableClassListForTransferUC;
import com.almeja.pel.gestao.core.domain.usecase.clazz.GetAvailableClassListUC;
import com.almeja.pel.gestao.core.domain.usecase.clazz.GetClassInfoUC;
import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;
import com.almeja.pel.gestao.inbound.http.interfaces.IClassController;
import com.almeja.pel.gestao.infra.context.AuthContext;
import com.almeja.pel.gestao.infra.dto.ClassDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClassController implements IClassController {

    private final GetAvailableClassListUC getAvailableClassListUC;
    private final GetClassInfoUC getClassInfoUC;
    private final GetAvailableClassListForTransferUC getAvailableClassListForTransferUC;

    @Override
    public List<IClassInfoDTO> getListAvailable(Integer course, Integer level) {
        return getAvailableClassListUC.execute(AuthContext.getUser(), course, level);
    }

    @Override
    public ClassDTO getDetails(Integer id) {
        return ConverterEntityToDTOUtil.convert(getClassInfoUC.execute(id), ClassDTO.class);
    }

    @Override
    public List<IClassInfoDTO> getAvailableForTransfer(Integer inscriptionId) {
        return getAvailableClassListForTransferUC.execute(inscriptionId);
    }

}
