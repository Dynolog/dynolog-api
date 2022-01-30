package com.github.appointmentsio.api.domain.project.form;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateProjectProps {

    @NotEmpty(message = "{project.name.notempty}")
    private String name;

    @DecimalMin(value = "0.1", inclusive = false, message = "{project.hourly-hate.decimalmin}")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "{project.hourly-rate.notnull}")
    private BigDecimal hourlyRate;

    @NotNull(message = "{project.currency.notnull}")
    private String currency;

    @NotNull(message = "{project.user-id.notnull}")
    private Long userId;
}