package com.almeja.pel.gestao.inbound.http.interfaces;

import com.almeja.pel.gestao.infra.dto.CourseDTO;
import com.almeja.pel.gestao.infra.dto.LevelDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.almeja.pel.gestao.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(ICourseController.PATH)
public interface ICourseController {

    String PATH = PREFIX_PATH + "/course";

    @GetMapping("/list-active")
    List<CourseDTO> getActiveCourseList();

    @GetMapping("/{id}/actual-level")
    LevelDTO getActualLevel(@PathVariable("id") Integer id);

}
