package com.github.dynolog.api.domain.session.service;

import static com.github.dynolog.api.utils.Constants.MESSAGES.CREATE_SESSION_ERROR_MESSAGE;
import static com.github.dynolog.api.utils.Constants.SECURITY.JWT;
import static com.github.dynolog.api.utils.Constants.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS;
import static com.github.dynolog.api.utils.Constants.SECURITY.TOKEN_EXPIRATION_IN_HOURS;
import static com.github.dynolog.api.utils.Constants.SECURITY.TOKEN_SECRET;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.forbidden;

import java.time.LocalDateTime;

import com.github.dynolog.api.domain.session.entity.RefreshToken;
import com.github.dynolog.api.domain.session.form.CreateTokenProps;
import com.github.dynolog.api.domain.session.model.TokenResponse;
import com.github.dynolog.api.domain.session.repository.RefreshTokenRepository;
import com.github.dynolog.api.domain.user.entity.User;
import com.github.dynolog.api.domain.user.service.FindUserService;

import com.github.dynolog.api.utils.Response;
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
                .orElseThrow(() -> Response.forbidden(message(CREATE_SESSION_ERROR_MESSAGE)));
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
