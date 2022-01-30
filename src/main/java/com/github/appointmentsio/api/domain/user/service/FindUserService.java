package com.github.appointmentsio.api.domain.user.service;

import com.github.appointmentsio.api.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.Optional;

import static com.github.appointmentsio.api.domain.user.repository.Queries.FIND_USER_DETAILS_BY_EMAIL_FETCH_ROLES;
import static com.github.appointmentsio.api.domain.user.repository.Queries.FIND_USER_DETAILS_BY_ID_FETCH_ROLES;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class FindUserService {

    @Autowired
    EntityManager manager;

    public Optional<User> findById(Long id) {
        var query = manager
            .createNativeQuery(FIND_USER_DETAILS_BY_ID_FETCH_ROLES, Tuple.class);

        query.setParameter("id", id);

        return  safeGetUser(query);
    }

    public Optional<User> findByEmail(String email) {
        var query = manager
            .createNativeQuery(FIND_USER_DETAILS_BY_EMAIL_FETCH_ROLES, Tuple.class);

        query.setParameter("email", email);

        return  safeGetUser(query);
    }

    private Optional<User> safeGetUser(Query query) {
        try {
            var tuple = (Tuple) query.getSingleResult();
            return of(new User(
                tuple.get("id", BigInteger.class).longValue(),
                tuple.get("name", String.class),
                tuple.get("email", String.class),
                tuple.get("password", String.class),
                tuple.get("roles", String.class)
            ));
        } catch (NoResultException exception) {
            return empty();
        }
    }
}
