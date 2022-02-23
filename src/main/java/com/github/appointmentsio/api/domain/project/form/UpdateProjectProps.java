package com.github.appointmentsio.api.domain.project.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProjectProps {
    @NotEmpty(message = "{project.name.notempty}")
    private String name;

    @DecimalMin(value = "0.1", inclusive = false, message = "{project.hourly-hate.decimalmin}")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "{project.hourly-rate.notnull}")
    private BigDecimal hourlyRate;

    @NotNull(message = "{project.currency.notnull}")
    private String currency;

    private String color;
}
