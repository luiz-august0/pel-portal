package com.almeja.pel.gestao.core.dto.interfaces;

import java.util.Date;

public interface IClassInfoDTO {

    Integer getId();

    String getProfessorName();

    String getDayOfWeek();

    Integer getAvailableSlots();

    Date getStartTime();

    Date getEndTime();

    String getClassName();

    Date getPlannedStartDate();

    String getRoomName();

    Date getPlannedEndDate();

    Integer getSubscribers();

}
