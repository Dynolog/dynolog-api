package com.github.throyer.apontamentos.domain.session.service;

import com.github.throyer.apontamentos.domain.session.dto.TokenRequest;
import com.github.throyer.apontamentos.domain.session.entity.RefreshToken;
import com.github.throyer.apontamentos.domain.session.repository.RefreshTokenRepository;
import com.github.throyer.apontamentos.domain.session.dto.TokenResponse;
import com.github.throyer.apontamentos.domain.user.dto.UserDetails;
import com.github.throyer.apontamentos.domain.user.repository.UserRepository;

import static com.github.throyer.apontamentos.utils.Constraints.JWT;
import static com.github.throyer.apontamentos.utils.Response.forbidden;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenService {
    
    public static final String CREATE_SESSION_ERROR_MESSAGE = "Senha ou Usuário inválidos.";

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration-in-hours}")
    private Integer TOKEN_EXPIRATION_IN_HOURS;

    @Value("${token.refresh.expiration-in-days}")
    private Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    
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
