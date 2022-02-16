package com.github.appointmentsio.api.domain.session.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RefreshToken", requiredProperties = {"accessToken", "refreshToken", "expiresIn", "tokenType"})
public class RefreshTokenResponse {
    private final String token;
    private final RefreshToken refreshToken;
    private final LocalDateTime expiresIn;

    public RefreshTokenResponse(
        String token,
        RefreshToken refreshToken,
        LocalDateTime expiresIn
    ) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    @JsonProperty("accessToken")
    public String getToken() {
        return token;
    }

    @JsonProperty("refreshToken")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonFormat(shape = Shape.STRING)
    @JsonProperty("expiresIn")
    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return  "Bearer";
    }
}
