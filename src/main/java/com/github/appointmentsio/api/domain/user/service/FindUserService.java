package com.github.appointmentsio.api.domain.user.service;

import static com.github.appointmentsio.api.domain.user.repository.Queries.FIND_USER_FETCH_ROLES_QUERY;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;

import com.github.appointmentsio.api.domain.user.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindUserService {

    @Autowired
    EntityManager manager;

    public Optional<User> findOptionalByIdFetchRoles(Long id) {

        var query = manager
                .createNativeQuery(format("%s\n%s", FIND_USER_FETCH_ROLES_QUERY, "where u.id = :id"), Tuple.class);

        query.setParameter("id", id);

        return  safeGetUserFromQuery(query);
    }

    public Optional<User> findOptionalByEmailFetchRoles(String email) {
        var query = manager
                .createNativeQuery(format("%s\n%s", FIND_USER_FETCH_ROLES_QUERY, "where u.email = :email"), Tuple.class);

        query.setParameter("email", email);

        return  safeGetUserFromQuery(query);
    }

    public Optional<User> findOptionalByNanoidFetchRoles(String nanoId) {
        var query = manager
                .createNativeQuery(format("%s\n%s", FIND_USER_FETCH_ROLES_QUERY, "where u.nano_id = :user_nano_id"), Tuple.class);

        query.setParameter("user_nano_id", nanoId.getBytes(UTF_8));

        return  safeGetUserFromQuery(query);
    }

    private Optional<User> safeGetUserFromQuery(Query query) {
        try {
            var tuple = (Tuple) query.getSingleResult();
            return of(new User(
                    tuple.get("id", BigInteger.class).longValue(),
                    tuple.get("nano_id", byte[].class),
                    tuple.get("name", String.class),
                    tuple.get("email", String.class),
                    tuple.get("password", String.class),
                    tuple.get("timezone", String.class),
                    tuple.get("date_format", String.class),
                    tuple.get("time_format", String.class),
                    tuple.get("roles", String.class)
            ));
        } catch (NoResultException exception) {
            return empty();
        }
    }
}