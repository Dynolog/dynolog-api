package com.github.dynolog.api.domain.timeentry.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Currencies {

    @Schema(example = "BRL", required = true)
    private final String currency;

    @Schema(example = "29.99", required = true)
    private final BigDecimal amount;

    public Currencies(String currency, List<ProjectSummary> projects) {
        this.currency = currency;
        this.amount = projects.stream()
            .map(ProjectSummary::getBillableHoursAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
