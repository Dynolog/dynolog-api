package com.github.appointmentsio.api.domain.session.service;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.CREATE_SESSION_ERROR_MESSAGE;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.JWT;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_EXPIRATION_IN_HOURS;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_SECRET;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.forbidden;

import java.time.LocalDateTime;

import com.github.appointmentsio.api.domain.session.entity.RefreshToken;
import com.github.appointmentsio.api.domain.session.form.CreateTokenProps;
import com.github.appointmentsio.api.domain.session.model.TokenResponse;
import com.github.appointmentsio.api.domain.session.repository.RefreshTokenRepository;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.service.FindUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final FindUserService findUserService;

    @Autowired
    public CreateTokenService(
        RefreshTokenRepository refreshTokenRepository,
        FindUserService findUserService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.findUserService = findUserService;
    }


    public TokenResponse create(CreateTokenProps request) {
        var userFound = findUserService.findOptionalByEmailFetchRoles(request.getEmail())
            .filter(user -> user.validatePassword(request.getPassword()))
                .orElseThrow(() -> forbidden(message(CREATE_SESSION_ERROR_MESSAGE)));
        return create(userFound);
    }

    public TokenResponse create(User user) {

        var now = LocalDateTime.now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

        var token = JWT.encode(user, expiresIn, TOKEN_SECRET);
        var refresh = new RefreshToken(user.getId(), REFRESH_TOKEN_EXPIRATION_IN_DAYS);

        refreshTokenRepository.disableOldRefreshTokens(user.getId());

        refreshTokenRepository.save(refresh);

        return new TokenResponse(
            user,
            token,
            refresh,
            expiresIn
        );
    }
}
