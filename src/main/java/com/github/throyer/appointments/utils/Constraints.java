package com.github.throyer.appointments.utils;

import com.github.throyer.appointments.domain.session.service.JsonWebToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Constraints {

    private Constraints(
        @Value("${token.secret}") String tokenSecret,
        @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
        @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays
    ) {
        Constraints.SECURITY.TOKEN_SECRET = tokenSecret;
        Constraints.SECURITY.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        Constraints.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;
    }

    public static class SECURITY {
        public static String TOKEN_SECRET;
        public static Integer TOKEN_EXPIRATION_IN_HOURS;
        public static Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;
        public static final Integer PASSWORD_STRENGTH = 10;
        public static final JsonWebToken JWT = new JsonWebToken();
        public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(PASSWORD_STRENGTH);
        public static final String CREATE_SESSION_ERROR_MESSAGE = "Invalid Password or Username.";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "Refresh token expired or invalid.";
        public static final String INVALID_USERNAME = "Nome de usuário invalido.";
        public static final String ROLES_KEY_ON_JWT = "roles";
    }

    public static class CURRENCY {
        public static final int CURRENCY_SCALE = 2;
        public static final double HOURS_IN_MILLISECONDS = 3.6e+6;
    }
}