package com.github.appointmentsio.api.domain.session.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Token", requiredProperties = {"user", "access_token", "refresh_token", "expires_in", "token_type"})
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

    @JsonProperty("access_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("refresh_token")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonFormat(shape = Shape.STRING)
    @JsonProperty("expires_in")
    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return "Bearer";
    }
}
