package com.github.appointmentsio.api.utils;

public class Time {

    /**
     * turn millis to time HH:mm:ss
     * @param millis
     * @return HH:mm:ss
     */
    public static String format(Long millis) {
        var secs = millis / 1000;
        return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }
}
