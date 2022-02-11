package com.github.appointmentsio.api.middlewares;

import com.github.appointmentsio.api.domain.session.service.SessionService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.appointmentsio.api.utils.Constraints.SECURITY.*;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Component
@Order(1)
public class AuthorizationMiddleware extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filter
    )
        throws ServletException, IOException {

        ofNullable(authorization(request))
            .ifPresent(SessionService::authorize);

        filter.doFilter(request, response);
    }
    
    private static String authorization(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token)) {
            return null;            
        }
        
        if (token.substring(7).equals(SECURITY_TYPE)) {
            return null;
        }

        return token.substring(7);
    }
    
    private static boolean tokenIsNull(String token) {
        return isNull(token) || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
    }    
}