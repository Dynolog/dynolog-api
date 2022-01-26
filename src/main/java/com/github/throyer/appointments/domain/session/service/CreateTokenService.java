package com.github.throyer.appointments.domain.session.service;

import com.github.throyer.appointments.domain.session.dto.TokenRequest;
import com.github.throyer.appointments.domain.session.dto.TokenResponse;
import com.github.throyer.appointments.domain.session.entity.RefreshToken;
import com.github.throyer.appointments.domain.session.repository.RefreshTokenRepository;
import com.github.throyer.appointments.domain.user.model.SimplifiedUser;
import com.github.throyer.appointments.domain.user.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static com.github.throyer.appointments.utils.Constraints.SECURITY.*;
import static com.github.throyer.appointments.utils.Response.forbidden;

@Service
public class CreateTokenService {

    @Autowired
    public CreateTokenService(
        RefreshTokenRepository refreshTokenRepository,
        FindUserService findUserService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.findUserService = findUserService;
    }

    private final RefreshTokenRepository refreshTokenRepository;
    private final FindUserService findUserService;
    
    public TokenResponse create(TokenRequest request) {
        var userFound = findUserService.findByEmail(request.getEmail())
            .filter(user -> user.validatePassword(request.getPassword()))
                .orElseThrow(() -> forbidden(CREATE_SESSION_ERROR_MESSAGE));
        return create(new SimplifiedUser(userFound));
    }

    public TokenResponse create(SimplifiedUser user) {

        var now = LocalDateTime.now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

        var token = JWT.encode(user, expiresIn, TOKEN_SECRET);
        var refresh = new RefreshToken(user, REFRESH_TOKEN_EXPIRATION_IN_DAYS);

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
