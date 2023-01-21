package ru.job4j.todo.util;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Вспомогательный класс для представления времени
 */
public class LdtConvert {
    /**
     * Преобразует {@param created} с учетом {@param created} в строку
     * @param userZone - строка идентификатора часового пояса
     * (<a href="https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html#getId--">
     *                 ZoneId.getId()</a>)
     * @param created - {@link LocalDateTime} время создания задачи
     *                хранимого в таблице БД (привязано к времени сервера).
     * @return {@link String} строка в формате: HH:mm yyyy-MM-dd
     */
    public static String toStringWithTimeZone(String userZone, @NotNull LocalDateTime created) {
        String timeZone = (userZone == null || "".equals(userZone))
                ? TimeZone.getDefault().getID() : userZone;
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime zdtAtZoneId = created.atZone(zoneId);
        return zdtAtZoneId.withZoneSameInstant(
                ZoneId.of(timeZone)
        ).format(DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd"));
    }
}
