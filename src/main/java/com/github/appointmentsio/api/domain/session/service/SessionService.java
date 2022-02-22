package com.github.appointmentsio.api.domain.session.service;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.INVALID_USERNAME;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.JWT;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.PUBLIC_ROUTES;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_SECRET;
import static com.github.appointmentsio.api.utils.Response.expired;
import static com.github.appointmentsio.api.utils.Response.forbidden;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.appointmentsio.api.domain.session.model.Authorized;
import com.github.appointmentsio.api.domain.user.service.FindUserService;
import com.github.appointmentsio.api.utils.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements UserDetailsService {

    @Autowired
    FindUserService findUserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = findUserService.findOptionalByEmailFetchRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));

        return new Authorized(user);
    }

    public static void authorize(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        if (PUBLIC_ROUTES.anyMatch(request)) {
            return;
        }

        var token = Authorization.extract(request);

        if (isNull(token)) {
            return;
        }

        try {
            var authorized = JWT.decode(token, TOKEN_SECRET);
            SecurityContextHolder
                .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            expired(response);
        }
    }

    public static Authorized authorizedOrThrow() {
        return authorized()
                .orElseThrow(() -> forbidden("Forbidden"));
    }

    public static Optional<Authorized> authorized() {
        try {
            var principal = getPrincipal();

            if (nonNull(principal) && principal instanceof Authorized authorized) {
                return of(authorized);
            }
            return empty();
        } catch (Exception exception) {
            return empty();
        }
    }

    private static Object getPrincipal() {
        var authentication = SecurityContextHolder
            .getContext()
                .getAuthentication();

        if (nonNull(authentication)) {
            return authentication.getPrincipal();
        }
        
        return null;
    }
}
