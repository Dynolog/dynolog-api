package com.github.throyer.appointments.domain.timeentry.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

@Data
public class Summary {
    private final String totalTime;
    private final BigDecimal billableHoursAmount;

    public Summary(List<TimeEntryDetails> entries) {
        long totalMillis = entries.stream()
            .map(TimeEntryDetails::totalTimeInMillis)
                .reduce(0L, Long::sum);

        var secs = totalMillis / 1000;
        this.totalTime = format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);

        var hours = totalMillis / 3.6e+6;
        var billableHours = hours * 60;
        this.billableHoursAmount = BigDecimal.valueOf(billableHours);
    }
}
