package com.github.throyer.appointments.domain.session.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {

    @NotEmpty(message = "email is a required field.")
    private String email;

    @NotEmpty(message = "password is a required field.")
    private String password;
}
