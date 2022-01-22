package com.github.throyer.appointments.domain.project.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateProjectProps {

    @NotEmpty(message = "Name is a required field.")
    private String name;

    @NotNull(message = "Hourly rate is a required field.")
    private BigDecimal hourlyHate;

    @NotNull(message = "User id is a required field.")
    private Long userId;
}
