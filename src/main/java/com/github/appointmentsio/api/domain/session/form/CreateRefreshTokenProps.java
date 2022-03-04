package com.github.appointmentsio.api.domain.session.form;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateRefreshTokenProps {

    @Schema(example = "d67befed-bfde-4de4-b7a0-72c9a92667e5")
    @NotNull(message = "{token.refresh-token.notnull}")
    @JsonProperty("refreshToken")
    private String refresh;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
