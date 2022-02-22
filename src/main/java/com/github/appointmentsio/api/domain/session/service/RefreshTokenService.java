package com.github.appointmentsio.api.domain.session.service;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.REFRESH_SESSION_ERROR_MESSAGE;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.JWT;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_EXPIRATION_IN_HOURS;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_SECRET;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.forbidden;
import static java.time.LocalDateTime.now;

import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import com.github.appointmentsio.api.domain.session.form.CreateRefreshTokenProps;
import com.github.appointmentsio.api.domain.session.model.RefreshTokenResponse;
import com.github.appointmentsio.api.domain.session.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponse refresh(CreateRefreshTokenProps request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(RefreshToken::nonExpired)
                .orElseThrow(() -> forbidden(message(REFRESH_SESSION_ERROR_MESSAGE)));

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
