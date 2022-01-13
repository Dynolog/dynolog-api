package com.github.throyer.apontamentos.utils;

import com.github.throyer.apontamentos.domain.session.service.JsonWebToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Constraints {
    private Constraints() { }
    public static final Integer PASSWORD_STRENGTH = 10;
    public static final JsonWebToken JWT = new JsonWebToken();
    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(PASSWORD_STRENGTH);
}
