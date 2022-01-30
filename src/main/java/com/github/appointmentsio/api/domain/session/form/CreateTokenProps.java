package com.github.appointmentsio.api.domain.session.form;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateTokenProps {

    @NotEmpty(message = "{token.email.notnull}")
    private String email;

    @NotEmpty(message = "{token.password.notnull}")
    private String password;
}
