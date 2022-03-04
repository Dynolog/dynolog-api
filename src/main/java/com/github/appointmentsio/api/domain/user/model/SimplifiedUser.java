package com.github.appointmentsio.api.domain.user.model;

import com.github.appointmentsio.api.domain.shared.model.Addressable;
import com.github.appointmentsio.api.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class SimplifiedUser implements Addressable {

    @Schema(description = "nano id", example = "V1StGXR8_Z5jdHi6B-myT", required = true)
    private final String id;

    @Schema(example = "Jubileu", required = true)
    private final String name;

    @Schema(example = "jubileu@email.com", required = true)
    private final String email;

    @Schema(example = "UTC", required = true)
    private final String timezone;

    @Schema(example = "dd/MM/yyyy", required = true)
    private final String dateFormat;

    @Schema(example = "HH:mm:ss", required = true)
    private final String timeFormat;

    @Schema(example = "[\"ADM\", \"USER\"]", required = true)
    private final List<String> roles;

    @Override
    public String getEmail() {
        return this.email;
    }

    public SimplifiedUser(User user) {
        this.id = user.getNanoId();
        this.name = user.getName();
        this.email = user.getEmail();

        this.timezone = user.getTimezone();
        this.dateFormat = user.getDateFormat();
        this.timeFormat = user.getTimeFormat();

        if (user.getRoles().isEmpty()) {
            this.roles = List.of();
        } else {
            this.roles = user.getRoles();
        }
    }
}
