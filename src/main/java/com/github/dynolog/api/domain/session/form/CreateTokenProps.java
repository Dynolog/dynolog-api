package com.github.dynolog.api.domain.session.form;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateTokenProps {

    @Schema(example = "admin@email.com")
    @NotEmpty(message = "{token.email.notnull}")
    private String email;

    @Schema(example = "12345678")
    @NotEmpty(message = "{token.password.notnull}")
    private String password;
}
