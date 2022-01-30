package com.github.appointmentsio.api.domain.session.service;

import com.github.appointmentsio.api.domain.session.model.Authorized;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.INVALID_USERNAME;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TOKEN_EXPIRED_OR_INVALID;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.JWT;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.TOKEN_SECRET;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.forbidden;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.logging.Level.WARNING;

@Service
public class SessionService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    private static final Logger LOGGER = Logger.getLogger(SessionService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findOptionalByEmailFetchRoles(email)
            .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        
        return new Authorized(user);
    }

    public static void authorize(String token) {
        try {
            var authorized = JWT.decode(token, TOKEN_SECRET);
            SecurityContextHolder
                .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            LOGGER.log(WARNING, message(TOKEN_EXPIRED_OR_INVALID));
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
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (nonNull(authentication)) {
            return authentication.getPrincipal();
        }
        return null;
    }
}
