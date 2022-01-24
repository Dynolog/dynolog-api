package com.github.throyer.appointments.domain.session.service;

import com.github.throyer.appointments.domain.session.model.Authorized;
import com.github.throyer.appointments.domain.user.repository.UserRepository;

import static com.github.throyer.appointments.utils.Constraints.SECURITY.JWT;
import static com.github.throyer.appointments.utils.Response.forbidden;
import static java.util.Objects.nonNull;
import java.util.Optional;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.logging.Level.WARNING;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements UserDetailsService {

    @Autowired
    UserRepository repository;
    private static final Logger LOGGER = Logger.getLogger(SessionService.class.getName());
    
    public static final String INVALID_USERNAME = "Nome de usuário invalido.";

    private static String SECRET;

    public SessionService(@Value("${token.secret}") String secret) {
        SessionService.SECRET = secret;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findOptionalByEmailFetchRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        
        return new Authorized(user);
    }

    public static void authorize(String token) {
        try {
            var authorized = JWT.decode(token, SECRET);
            SecurityContextHolder
                .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            LOGGER.log(WARNING, "Token expired or invalid");
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
