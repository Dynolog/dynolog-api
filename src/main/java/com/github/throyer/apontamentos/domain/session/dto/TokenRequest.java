package com.github.throyer.apontamentos.domain.session.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequest {

    @NotNull(message = "O Email não pode ser NULO.")
    @NotEmpty(message = "Email invalido.")
    private String email;

    @NotNull(message = "A Senha não pode ser NULA.")
    @NotEmpty(message = "Senha invalida.")
    private String password;
}
