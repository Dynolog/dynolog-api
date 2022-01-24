package com.github.throyer.appointments.domain.session.service;

import com.github.throyer.appointments.domain.session.dto.TokenRequest;
import com.github.throyer.appointments.domain.session.dto.TokenResponse;
import com.github.throyer.appointments.domain.session.entity.RefreshToken;
import com.github.throyer.appointments.domain.session.repository.RefreshTokenRepository;
import com.github.throyer.appointments.domain.user.model.UserDetails;
import com.github.throyer.appointments.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.throyer.appointments.utils.Constraints.SECURITY.*;
import static com.github.throyer.appointments.utils.Response.forbidden;

@Service
public class CreateTokenService {

    @Autowired
    public CreateTokenService(
        RefreshTokenRepository refreshTokenRepository,
        UserRepository userRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public TokenResponse create(TokenRequest request) {
        var user = userRepository.findOptionalByEmailFetchRoles(request.getEmail())
            .filter(session -> session.validatePassword(request.getPassword()))
                .orElseThrow(() -> forbidden(CREATE_SESSION_ERROR_MESSAGE));
        return create(new UserDetails(user));
    }

    public TokenResponse create(UserDetails user) {

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
