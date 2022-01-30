package com.github.appointmentsio.api.domain.user.model;

import com.github.appointmentsio.api.domain.shared.Addressable;
import com.github.appointmentsio.api.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class SimplifiedUser implements Addressable {
    private final Long id;
    private final String name;
    private final String email;
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
