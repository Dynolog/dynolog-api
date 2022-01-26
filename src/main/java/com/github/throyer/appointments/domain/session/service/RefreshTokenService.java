package com.github.throyer.appointments.domain.session.service;

import com.github.throyer.appointments.domain.session.dto.RefreshTokenRequest;
import com.github.throyer.appointments.domain.session.dto.RefreshTokenResponse;
import com.github.throyer.appointments.domain.session.entity.RefreshToken;
import com.github.throyer.appointments.domain.session.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import static com.github.throyer.appointments.utils.Constraints.SECURITY.*;
import static com.github.throyer.appointments.utils.Response.forbidden;
import static java.time.LocalDateTime.now;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(RefreshToken::nonExpired)
                .orElseThrow(() -> forbidden(REFRESH_SESSION_ERROR_MESSAGE));

        var now = now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);
        var token = JWT.encode(old.getUser(), expiresIn, TOKEN_SECRET);

        refreshTokenRepository.disableOldRefreshTokens(old.getUser().getId());

        var refresh = refreshTokenRepository.save(new RefreshToken(old.getUser(), REFRESH_TOKEN_EXPIRATION_IN_DAYS));

        return new RefreshTokenResponse(
            token,
            refresh,
            expiresIn
        );
    }
}
