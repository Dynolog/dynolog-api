package com.github.throyer.appointments.domain.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.throyer.appointments.domain.shared.Addressable;
import com.github.throyer.appointments.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
public class SimplifiedUser implements Addressable {
    private final Long id;
    private final String name;

    @JsonInclude(NON_NULL)
    private final String email;

    @JsonInclude(NON_NULL)
    private final List<String> roles;

    @Override
    public String getEmail() {
        return this.email;
    }

    public SimplifiedUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();

        if (user.getRoles().isEmpty()) {
            this.roles = null;
        } else {
            this.roles = user.getRoles();
        }
    }
}
