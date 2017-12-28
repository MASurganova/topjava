package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Component
public class TimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
       return parseLocalTime(s);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.format(DateTimeFormatter.ISO_TIME);
    }
}
