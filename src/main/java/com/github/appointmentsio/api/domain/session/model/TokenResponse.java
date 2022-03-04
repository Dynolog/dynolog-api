package com.github.appointmentsio.api.domain.session.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.utils.Constants.PATTERNS.DATE_ISO_WITH_TIMEZONE;

@Getter
@Schema(name = "Token")
public class TokenResponse {

    @Schema(required = true)
    private final SimplifiedUser user;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", required = true)
    private final String accessToken;

    @Schema(example = "d67befed-bfde-4de4-b7a0-72c9a92667e5", required = true)
    private final RefreshToken refreshToken;

    @JsonFormat(timezone = "UTC", pattern = DATE_ISO_WITH_TIMEZONE)
    @Schema(example = "2022-02-14T00:32:00.000Z", required = true)
    private final LocalDateTime expiresIn;

    @Schema(example = "Bearer", required = true)
    private final String tokenType = "Bearer";

    public TokenResponse(
            User user,
            String token,
            RefreshToken refreshToken,
            LocalDateTime expiresIn
    ) {
        this.user = new SimplifiedUser(user);
        this.accessToken = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
