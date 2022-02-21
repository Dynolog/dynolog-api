package com.github.appointmentsio.api.middlewares;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static java.util.logging.Level.SEVERE;

public class PublicRouteMatcher {
    private Map<HttpMethod, String[]> routes = new HashMap<>();
    private List<AntPathRequestMatcher> matchers = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(PublicRouteMatcher.class.getName());

    private PublicRouteMatcher() { }

    public static PublicRouteMatcher create() {
        return new PublicRouteMatcher();
    }

    public PublicRouteMatcher add(HttpMethod method, String... routes) {
        this.routes.put(method, routes);
            asList(routes)
                .forEach(route -> matchers.add(new AntPathRequestMatcher(route, method.name())));
        return this;
    }

    public boolean anyMatch(HttpServletRequest request) {
        try {
            return this.matchers.stream().anyMatch(requestMatcher -> requestMatcher.matches(request));
        } catch (Exception exception) {
            LOGGER.log(SEVERE,"error on route matching", exception);
            return false;
        }
    }

    public void configure(HttpSecurity http) {
        routes.forEach((method, routes) -> {
            try {
                http
                    .antMatcher("/**")
                        .authorizeRequests()
                        .antMatchers(method, routes)
                    .permitAll();
            } catch (Exception exception) {
                LOGGER.log(SEVERE,"error on set public routes", exception);
            }
        });
    }
}
