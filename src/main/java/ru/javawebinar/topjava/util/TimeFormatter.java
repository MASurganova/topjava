package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class TimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
        LocalTime time = null;
        try {
            time = LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception ignore) {}
        return time;
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
