package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Component
public class DateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String s, Locale locale) throws ParseException {
        return parseLocalDate(s);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }
}
