package com.github.appointmentsio.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Schema(requiredProperties = {"currency", "amount"})
public class Currencies {
    private final String currency;
    private final BigDecimal amount;

    public Currencies(String currency, List<ProjectSummary> projects) {
        this.currency = currency;
        this.amount = projects.stream()
            .map(ProjectSummary::getBillableHoursAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
