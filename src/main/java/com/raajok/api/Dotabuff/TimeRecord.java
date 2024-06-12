package com.raajok.api.Dotabuff;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * Record where the value is time in HH:mm:ss format.
 */
public final class TimeRecord implements Record<String> {

    private final String title;
    private final String value;
    private final String author;

    public TimeRecord(String title, String value, String author) {
        this.title = title;
        this.value = value;
        this.author = author;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String author() {
        return this.author;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean isGreater(Record record) {
        LocalTime compareTime;
        try {
            compareTime = LocalTime.parse(record.value().toString(), DateTimeFormatter.ofPattern("H:mm:ss"));
        } catch (DateTimeParseException e) {
            return false;
        }

        int compareHour = compareTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int compareMinute = compareTime.get(ChronoField.MINUTE_OF_HOUR);
        int compareSecond = compareTime.get(ChronoField.SECOND_OF_MINUTE);

        LocalTime currentTime = LocalTime.parse(this.value, DateTimeFormatter.ofPattern("H:mm:ss"));
        int currentHour = currentTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int currentMinute = currentTime.get(ChronoField.MINUTE_OF_HOUR);
        int currentSecond = currentTime.get(ChronoField.SECOND_OF_MINUTE);

        if (currentHour > compareHour) {
            return true;
        } else if (compareHour > currentHour) {
            return false;
        } else if (currentMinute > compareMinute) {
            return true;
        } else if (compareMinute > currentMinute) {
            return false;
        } else return currentSecond > compareSecond;
    }
}
