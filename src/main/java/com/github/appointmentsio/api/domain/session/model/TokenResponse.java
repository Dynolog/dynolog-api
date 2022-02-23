package com.github.appointmentsio.api.domain.session.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Token", requiredProperties = {"user", "accessToken", "refreshToken", "expiresIn", "tokenType"})
public class TokenResponse {

    private final SimplifiedUser user;
    private final String token;
    private final RefreshToken refreshToken;
    private final LocalDateTime expiresIn;

    public TokenResponse(
            User user,
            String token,
            RefreshToken refreshToken,
            LocalDateTime expiresIn
    ) {
        this.user = new SimplifiedUser(user);
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public SimplifiedUser getUser() {
        return user;
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
        return "Bearer";
    }
}
