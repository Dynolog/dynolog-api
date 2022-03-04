package com.github.appointmentsio.api.domain.user.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.appointmentsio.api.domain.shared.model.Addressable;
import static com.github.appointmentsio.api.domain.user.validation.EmailUniqueness.validateEmailUniqueness;

@Data
@NoArgsConstructor
@Schema(name = "CreateUser")
public class CreateUserProps implements Addressable {

    @Schema(example = "Jubileu")
    @NotEmpty(message = "{user.name.notempty}")
    private String name;

    @Schema(example = "jubileu@email.com")
    @NotEmpty(message = "{user.email.notempty}")
    @Email(message = "{user.email.is-valid}")
    private String email;

    @Schema(example = "12345678")
    @NotEmpty(message = "{user.password.notempty}")
    @Size(min = 8, max = 155, message = "{user.password.size}")
    private String password;

    public void validate() {
        validateEmailUniqueness(this);
    }

    public CreateUserProps(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
