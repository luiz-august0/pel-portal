package com.almeja.pel.gestao.inbound.http.interfaces;

import com.almeja.pel.gestao.core.dto.RegisterLevelingDTO;
import com.almeja.pel.gestao.infra.dto.LevelingGroupedByYearDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.almeja.pel.gestao.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(ILevelingController.PATH)
public interface ILevelingController {

    String PATH = PREFIX_PATH + "/leveling";

    @GetMapping("/hours-available")
    List<Date> getAvailableHours(@RequestParam(name = "courseId") Integer courseId);

    @GetMapping("/dates-available")
    List<String> getAvailableDates(@RequestParam(name = "courseId") Integer courseId,
                                   @RequestParam(name = "levelingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date levelingDate);

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Integer register(@RequestBody RegisterLevelingDTO registerLevelingDTO);

    @DeleteMapping("/{levelingId}/cancel")
    void cancel(@PathVariable("levelingId") Integer levelingId);

    @GetMapping("/grouped-by-year")
    List<LevelingGroupedByYearDTO> getGroupedByYear();

}
