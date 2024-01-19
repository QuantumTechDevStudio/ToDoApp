package ru.todoapp.utils;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class TimeUtils {
    /**
     * method to check for correct formatting
     *
     * @param time to check for formatting
     */
    public static boolean isCorrectTimeFormat(String time) {
        try {
            Instant.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
