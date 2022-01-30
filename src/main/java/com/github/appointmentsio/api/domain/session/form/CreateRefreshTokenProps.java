package com.github.appointmentsio.api.domain.session.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateRefreshTokenProps {

    @NotNull(message = "{token.refresh-token.notnull}")
    @JsonProperty("refresh_token")
    private String refresh;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
