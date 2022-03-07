package com.github.dynolog.api.domain.project.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateProjectProps {

    @Schema(example = "Fake Project")
    @NotEmpty(message = "{project.name.notempty}")
    private String name;

    @Schema(example = "29.99")
    @DecimalMin(value = "0.1", inclusive = false, message = "{project.hourly-hate.decimalmin}")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "{project.hourly-rate.notnull}")
    private BigDecimal hourlyRate;

    @Schema(example = "BRL")
    @NotNull(message = "{project.currency.notnull}")
    private String currency;

    @Schema(example = "#fafafa")
    private String color = "#7B70EA";

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT")
    @NotNull(message = "{project.user-id.notnull}")
    private String userId;

    public CreateProjectProps(String name, BigDecimal hourlyRate, String currency, String userId) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.currency = currency;
        this.userId = userId;
    }
}