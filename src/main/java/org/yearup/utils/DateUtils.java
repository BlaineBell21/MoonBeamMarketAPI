package org.yearup.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String currentDateAndTimeString(){
        // returns String to be used in the receipt writer
        LocalDateTime dateAndTime = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");

        return dateAndTime.format(fmt);
    }

    public static LocalDateTime currentDateAndTime(){
        // returns LocalDateTime type to be recorded directly to database
        return LocalDateTime.now();
    }
}
