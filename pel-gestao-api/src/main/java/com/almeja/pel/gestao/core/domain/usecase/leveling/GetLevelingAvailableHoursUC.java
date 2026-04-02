package com.almeja.pel.gestao.core.domain.usecase.leveling;

import com.almeja.pel.gestao.core.gateway.repository.LevelingScheduleRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLevelingAvailableHoursUC {

    private final LevelingScheduleRepositoryGTW levelingScheduleRepositoryGTW;

    public List<Date> execute(Integer courseId) {
        return levelingScheduleRepositoryGTW.findAllAvailableHoursByCourse(courseId);
    }

}
