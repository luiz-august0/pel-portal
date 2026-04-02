package com.almeja.pel.portal.core.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final TimeZone TIME_ZONE_BRAZIL = TimeZone.getTimeZone("America/Sao_Paulo");

    public static Date getDate() {
        return getCalendar().getTime();
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance(TIME_ZONE_BRAZIL);
    }

    public static int getAge(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthLocalDate, currentDate).getYears();
    }

}
