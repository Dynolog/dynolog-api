package com.github.throyer.appointments.domain.project.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateProjectProps {
    @NotEmpty(message = "Name is a required field.")
    private String name;

    @DecimalMin(value = "0.1", inclusive = false, message = "cannot be less than 0.1")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "Hourly rate is a required field.")
    private BigDecimal hourlyHate;
}
