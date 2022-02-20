package com.github.appointmentsio.api.domain.session.form;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CreateTokenProps {

    @NotEmpty(message = "{token.email.notnull}")
    private String email;

    @NotEmpty(message = "{token.password.notnull}")
    private String password;

    public CreateTokenProps(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
