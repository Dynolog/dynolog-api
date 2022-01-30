package com.github.appointmentsio.api.domain.user.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.appointmentsio.api.domain.shared.Addressable;
import static com.github.appointmentsio.api.domain.user.validation.EmailUniqueness.validateEmailUniqueness;

@Data
@NoArgsConstructor
public class CreateUserProps implements Addressable {
    @NotEmpty(message = "{user.name.notempty}")
    private String name;

    @NotEmpty(message = "{user.email.notempty}")
    @Email(message = "{user.email.is-valid}")
    private String email;

    @NotEmpty(message = "{user.password.notempty}")
    @Size(min = 8, max = 155, message = "{user.password.size}")
    private String password;


    public void validate() {
        validateEmailUniqueness(this);
    }
}