package com.raajok.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Time represented as a unix epoch.
 */
public class EpochTime {

    private int time;

    public EpochTime(int time) {
        this.time = time;
    }

    /**
     * Convert the epoch number into dd.MM.yyyy date.
     * @return Date String
     */
    public String asDate() {
        if (this.time == 0) {
            return "Never";
        }

        Date date = new Date(this.time * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setTimeZone(TimeZone.getDefault());
        String dateString = format.format(date);

        return dateString;
    }
}
