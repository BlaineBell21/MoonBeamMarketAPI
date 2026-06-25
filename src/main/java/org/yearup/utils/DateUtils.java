package org.yearup.utils;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String currentDateAndTimeString(){
        // used to get current, local date and time when making a new transaction
        LocalDateTime dateAndTime = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss");
        // formatted as shown in capstone example
        return dateAndTime.format(fmt);
        // returns current, local date and time
    }

    public static LocalDateTime currentDateAndTime(){
        return LocalDateTime.now();
    }
}
