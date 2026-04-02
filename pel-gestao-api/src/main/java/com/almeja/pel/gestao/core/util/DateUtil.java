package com.almeja.pel.gestao.core.util;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
        LocalDate birthLocalDate = LocalDate.parse(birthDate.toString());
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthLocalDate, currentDate).getYears();
    }

    public static Integer getYear(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getExtensionDate(Date date) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.of("pt", "BR"));
        return formatter.format(date);
    }

}
