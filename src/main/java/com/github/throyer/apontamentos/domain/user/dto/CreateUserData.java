package com.github.throyer.apontamentos.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.throyer.apontamentos.domain.shared.Addressable;
import static com.github.throyer.apontamentos.domain.user.validation.EmailUniqueness.validateEmailUniqueness;

@Data
@NoArgsConstructor
public class CreateUserData implements Addressable {

    public static final String DEFAULT_PASSWORD = "mudar123";

    @NotEmpty(message = "Por favor, forneça um nome.")
    private String name;

    @NotEmpty(message = "Por favor, forneça um e-mail.")
    @Email(message = "Por favor, forneça um e-mail valido.")
    private String email;

    @NotEmpty(message = "Por favor, forneça uma senha.")
    @Size(min = 8, max = 155, message = "A senha deve conter no minimo {min} caracteres.")
    private String password = DEFAULT_PASSWORD;

    public void validate() {
        validateEmailUniqueness(this);
    }
}
