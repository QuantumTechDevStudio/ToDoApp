package ru.todoapp.utils;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
    /**
     * method to check for correct formatting of the time (date-time)
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

    /**
     * method to check for correct formatting of the date
     *
     * @param date to check for formatting
     */
    public static boolean isCorrectDateFormat(String date) {
        try {
            String[] split = date.split("\\+");
            Instant.parse(split[0] + "T00:00:00+" + split[1]);
            return true;
        } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
            return false;
        }
    }

    //Ugly-ass methods, but the only solutions to the problem I managed to find

    /**
     * The method that sets time of the start of the day (00:00:00) into date string and returns Instant.
     * This method is similar to the LocalDate class method, except it does not remove the timezone.
     *
     * @param date - date String that does not contain time in ISO_OFFSET_DATE format ("yyyy-MM-ddx")
     */
    public static Instant setAtTheStartOfTheDay(String date) {
        String[] split = date.split("\\+");
        return Instant.parse(split[0] + "T00:00:00+" + split[1]);
    }

    /**
     * The method that sets time of the end of the day (24:00:00) into date string and returns Instant.
     * This method is similar to the LocalDate class method, except it does not remove the timezone.
     *
     * @param date - date String that does not contain time in ISO_OFFSET_DATE format ("yyyy-MM-ddx")
     */
    public static Instant setAtTheEndOfTheDay(String date) {
        String[] split = date.split("\\+");
        return Instant.parse(split[0] + "T23:59:59+" + split[1]);
    }
}
