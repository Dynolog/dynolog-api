package com.github.throyer.appointments.domain.session.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {

    @NotNull(message = "O Email não pode ser NULO.")
    @NotEmpty(message = "Email invalido.")
    private String email;

    @NotNull(message = "A Senha não pode ser NULA.")
    @NotEmpty(message = "Senha invalida.")
    private String password;
}
