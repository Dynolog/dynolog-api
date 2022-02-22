package com.github.appointmentsio.api.middlewares;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorize;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class AuthorizationMiddleware extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filter
    ) throws ServletException, IOException {
        authorize(request, response);
        filter.doFilter(request, response);
    }
}