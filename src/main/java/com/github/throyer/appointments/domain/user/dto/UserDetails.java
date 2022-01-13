package com.github.throyer.appointments.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.throyer.appointments.domain.user.entity.User;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.throyer.appointments.domain.shared.Addressable;

@Data
@NoArgsConstructor
public class UserDetails implements Addressable {

    private Long id;
    private String name;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles;

    public UserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoleInitials();
    }

    public UserDetails(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = null;
    }
}
