package com.example.demo.data;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Double parseDouble(String value, NumberFormat format) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return format.parse(value).doubleValue();
        } catch (ParseException e) {
            System.out.println("ERROR WITH PARSING: " + e.getMessage());
            return null;
        }
    }

    public static LocalDate parseDate(String text, String pattern) {
        if (text == null || text.trim().isEmpty()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("ERROR WITH PARSING: " + e.getMessage());
            return null;
        }
    }

    public static Integer parseInteger(String value, NumberFormat format) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return format.parse(value).intValue();
        } catch (ParseException e) {
            System.out.println("ERROR WITH PARSING: " + e.getMessage());
            return null;
        }
    }

}
