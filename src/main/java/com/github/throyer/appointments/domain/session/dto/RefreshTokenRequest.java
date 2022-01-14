package com.github.throyer.appointments.domain.session.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {

    @NotNull(message = "refresh_token is a required field.")
    @NotEmpty(message = "refresh_token invalid.")
    @JsonProperty("refresh_token")
    private String refresh;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
