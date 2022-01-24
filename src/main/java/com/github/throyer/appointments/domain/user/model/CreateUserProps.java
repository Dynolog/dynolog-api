package com.github.throyer.appointments.domain.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.throyer.appointments.domain.shared.Addressable;
import static com.github.throyer.appointments.domain.user.validation.EmailUniqueness.validateEmailUniqueness;

@Data
@NoArgsConstructor
public class CreateUserProps implements Addressable {
    @NotEmpty(message = "email is a required field.")
    private String name;

    @NotEmpty(message = "email is a required field.")
    @Email(message = "invalid email.")
    private String email;

    @NotEmpty(message = "password is a required field.")
    @Size(min = 8, max = 155, message = "The password must contain at least {min} characters.")
    private String password;

    public void validate() {
        validateEmailUniqueness(this);
    }
}
