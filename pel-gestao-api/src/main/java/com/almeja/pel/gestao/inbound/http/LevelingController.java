package com.almeja.pel.gestao.inbound.http;

import com.almeja.pel.gestao.core.domain.usecase.leveling.*;
import com.almeja.pel.gestao.core.dto.RegisterLevelingDTO;
import com.almeja.pel.gestao.inbound.http.interfaces.ILevelingController;
import com.almeja.pel.gestao.infra.context.AuthContext;
import com.almeja.pel.gestao.infra.dto.LevelingGroupedByYearDTO;
import com.almeja.pel.gestao.infra.dto.mapper.LevelingGroupedByYearMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LevelingController implements ILevelingController {

    private final GetLevelingAvailableHoursUC getLevelingAvailableHoursUC;
    private final RegisterLevelingUC registerLevelingUC;
    private final CancelLevelingUC cancelLevelingUC;
    private final GetLevelingGroupedByYearUC getLevelingGroupedByYearUC;
    private final GetLevelingAvailableDatesUC getLevelingAvailableDatesUC;

    @Override
    public List<Date> getAvailableHours(Integer courseId) {
        return getLevelingAvailableHoursUC.execute(courseId);
    }

    @Override
    public List<String> getAvailableDates(Integer courseId, Date levelingDate) {
        return getLevelingAvailableDatesUC.execute(courseId, levelingDate);
    }

    @Override
    public Integer register(RegisterLevelingDTO registerLevelingDTO) {
        return registerLevelingUC.execute(AuthContext.getUser(), registerLevelingDTO);
    }

    @Override
    public void cancel(Integer levelingId) {
        cancelLevelingUC.execute(AuthContext.getUser(), levelingId);
    }

    @Override
    public List<LevelingGroupedByYearDTO> getGroupedByYear() {
        return LevelingGroupedByYearMapper.toDTO(getLevelingGroupedByYearUC.execute(AuthContext.getUser()));
    }

}

