package com.github.dynolog.api.domain.user.validation;

import com.github.dynolog.api.domain.shared.model.Addressable;
import com.github.dynolog.api.domain.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailUniqueness {

    private static final String MESSAGE = "This email has already been used by another user. Please use a different email.";

    private static UserRepository repository;
  
    @Autowired
    public EmailUniqueness(UserRepository repository) {
        EmailUniqueness.repository = repository;
    }

    public static void validateEmailUniqueness(Addressable entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE);
        }
    }

    public static void validateEmailUniquenessOnModify(Addressable newEntity, Addressable actualEntity) {

        var newEmail = newEntity.getEmail();
        var actualEmail = actualEntity.getEmail();

        var changedEmail = !actualEmail.equals(newEmail);

        var emailAlreadyUsed = repository.existsByEmail(newEmail);

        if (changedEmail && emailAlreadyUsed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE);
        }
    }
}
