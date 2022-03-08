package com.github.dynolog.api.domain.shared.model;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.github.dynolog.api.utils.Constants.SECURITY.ERROR_CHECKING_IF_THE_REQUEST_IS_PUBLIC_ROUTE_MESSAGE;
import static com.github.dynolog.api.utils.Constants.SECURITY.UNABLE_TO_INJECT_PUBLIC_API_ROUTES_MESSAGE;
import static java.util.Arrays.asList;
import static java.util.logging.Level.SEVERE;

public class PublicRoutes {
    private final Map<HttpMethod, String[]> routes = new HashMap<>();
    private final List<AntPathRequestMatcher> matchers = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(PublicRoutes.class.getName());

    private PublicRoutes() {
    }

    public static PublicRoutes create() {
        return new PublicRoutes();
    }

    public PublicRoutes add(HttpMethod method, String... routes) {
        this.routes.put(method, routes);
        asList(routes)
                .forEach(route -> matchers.add(new AntPathRequestMatcher(route, method.name())));
        return this;
    }

    public boolean anyMatch(HttpServletRequest request) {
        try {
            return this.matchers.stream().anyMatch(requestMatcher -> requestMatcher.matches(request));
        } catch (Exception exception) {
            LOGGER.log(SEVERE, ERROR_CHECKING_IF_THE_REQUEST_IS_PUBLIC_ROUTE_MESSAGE, exception);
            return false;
        }
    }

    public void injectOn(HttpSecurity http) {
        routes.forEach((method, routes) -> {
            try {
                http
                    .antMatcher("/**")
                        .authorizeRequests()
                            .antMatchers(method, routes)
                                .permitAll();
            } catch (Exception exception) {
                LOGGER.log(SEVERE, UNABLE_TO_INJECT_PUBLIC_API_ROUTES_MESSAGE, exception);
            }
        });
    }
}
