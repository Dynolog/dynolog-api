package com.github.dynolog.api.utils;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.github.dynolog.api.domain.shared.model.PublicRoutes;
import com.github.dynolog.api.domain.session.service.JsonWebToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
public class Constants {

    private Constants(
            @Value("${token.secret}") String tokenSecret,
            @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
            @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays,
            @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}") Integer maxRequestsPerMinute
    ) {
        Constants.SECURITY.TOKEN_SECRET = tokenSecret;
        Constants.SECURITY.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        Constants.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;

        Constants.RATE_LIMIT.MAX_REQUESTS_PER_MINUTE = maxRequestsPerMinute;
    }

    public static class SECURITY {
        public static final PublicRoutes PUBLIC_ROUTES = PublicRoutes.create()
            .add(GET, "/", "/api", "/documentation/**", "/swagger-ui/**")
            .add(POST, "/api/sessions", "/api/sessions/refresh", "/api/users");

        public static final String ERROR_CHECKING_IF_THE_REQUEST_IS_PUBLIC_ROUTE_MESSAGE = "Error when checking if the request is in a public route";
        public static final String UNABLE_TO_INJECT_PUBLIC_API_ROUTES_MESSAGE = "Unable to inject public api routes";

        public static final String SECURITY_TYPE = "Bearer";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String ACCEPTABLE_TOKEN_TYPE = SECURITY_TYPE + " ";
        public static final String CAN_T_WRITE_RESPONSE_ERROR = "can't write response error.";

        public static final CorsConfiguration CORS_CONFIGURATION = new CorsConfiguration();

        public static String TOKEN_SECRET;
        public static Integer TOKEN_EXPIRATION_IN_HOURS;
        public static Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;
        public static final Integer PASSWORD_STRENGTH = 10;
        public static final JsonWebToken JWT = new JsonWebToken();
        public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(PASSWORD_STRENGTH);
        public static final String ROLES_KEY_ON_JWT = "roles";

        public static final String[] STATIC_FILES = {
            "/robots.txt",
            "/font/**",
            "/css/**",
            "/webjars/**",
            "/webjars/",
            "/js/**",
            "/favicon.ico",
            "/**.html",
            "/documentation/**"
        };
    }

    public static class CURRENCY {
        public static final int CURRENCY_SCALE = 2;
        public static final double HOURS_IN_MILLISECONDS = 3.6e+6;
    }

    public static class PATTERNS {
        public static final String DATE_ISO_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }

    public static class RATE_LIMIT {
        public static Integer MAX_REQUESTS_PER_MINUTE;
    }

    /**
     * Validation messages.
     * @see "resources/messages.properties"
     */
    public static class MESSAGES {
        public static final String CHECK_PROPERTY_ERRORS = "check.property.errors";

        public static final String NOT_AUTHORIZED = "not.authorized";

        public static final String NOT_AUTHORIZED_TO_LIST = "not.authorized.list";
        public static final String NOT_AUTHORIZED_TO_READ = "not.authorized.read";
        public static final String NOT_AUTHORIZED_TO_CREATE = "not.authorized.create";
        public static final String NOT_AUTHORIZED_TO_MODIFY = "not.authorized.modify";

        public static final String TYPE_MISMATCH_ERROR_MESSAGE = "type.mismatch.message";
        public static final String TOKEN_EXPIRED_OR_INVALID = "token.expired-or-invalid";
        public static final String TOKEN_HEADER_MISSING_MESSAGE = "token.header.missing";
        public static final String INVALID_USERNAME = "invalid.username.error.message";

        public static final String DATES_INTERVAL_CANNOT_LONGER_THAN_YEARS = "dates.interval.cannot-longer-than.years";
        public static final String DATES_INTERVAL_CANNOT_LONGER_THAN_MONTHS = "dates.interval.cannot-longer-than.months";
        public static final String SEARCH_DATE_INTERVAL_INVALID = "dates.interval.invalid";

        public static final String TIMEENTRY_DATE_INTERVAL_INVALID = "timeentry.interval.invalid";
        public static final String NO_TIMEENTRY_RUNNING = "timeentry.running.not-found";

        public static final String CREATE_SESSION_ERROR_MESSAGE = "create.session.error.message";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "refresh.session.error.message";
    }
}