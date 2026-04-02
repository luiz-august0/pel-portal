package com.almeja.pel.gestao.core.domain.usecase.leveling;

import com.almeja.pel.gestao.core.gateway.repository.LevelingScheduleRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLevelingAvailableDatesUC {

    private final LevelingScheduleRepositoryGTW levelingScheduleRepositoryGTW;

    public List<String> execute(Integer courseId, Date levelingDate) {
        return levelingScheduleRepositoryGTW.findAllAvailableDatesByCourseAndDate(courseId, levelingDate);
    }

}
