package com.almeja.pel.gestao.inbound.http;

import com.almeja.pel.gestao.core.domain.usecase.course.GetActiveCourseListUC;
import com.almeja.pel.gestao.core.domain.usecase.course.GetActualCourseLevelUC;
import com.almeja.pel.gestao.inbound.http.interfaces.ICourseController;
import com.almeja.pel.gestao.infra.context.AuthContext;
import com.almeja.pel.gestao.infra.dto.CourseDTO;
import com.almeja.pel.gestao.infra.dto.LevelDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController implements ICourseController {

    private final GetActiveCourseListUC getActiveCourseListUC;
    private final GetActualCourseLevelUC getActualCourseLevelUC;

    @Override
    public List<CourseDTO> getActiveCourseList() {
        return ConverterEntityToDTOUtil.convert(getActiveCourseListUC.execute(AuthContext.getUser()), CourseDTO.class);
    }

    @Override
    public LevelDTO getActualLevel(Integer id) {
        return ConverterEntityToDTOUtil.convert(getActualCourseLevelUC.execute(id, AuthContext.getUser()), LevelDTO.class);
    }

}
