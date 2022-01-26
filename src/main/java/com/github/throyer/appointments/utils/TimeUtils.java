package com.github.throyer.appointments.utils;

import static java.lang.String.format;

public class TimeUtils {

    /**
     * turn millis to time HH:mm:ss
     * @param millis
     * @return HH:mm:ss
     */
    public static String millisToTime(Long millis) {
        var secs = millis / 1000;
        return format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }
}
