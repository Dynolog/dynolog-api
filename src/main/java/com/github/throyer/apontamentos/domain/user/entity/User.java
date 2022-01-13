package com.github.throyer.apontamentos.domain.user.entity;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.apontamentos.domain.role.entity.Role;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.throyer.apontamentos.domain.shared.Addressable;
import com.github.throyer.apontamentos.domain.user.dto.CreateUserData;
import static com.github.throyer.apontamentos.utils.Constraints.PASSWORD_ENCODER;
import java.util.Optional;
import javax.persistence.PrePersist;

@Data
@Entity
@NoArgsConstructor
public class User implements Serializable, Addressable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String email;

    @JsonProperty(access = WRITE_ONLY)
    private String password;

    private Boolean active = true;

    @ManyToMany(cascade = DETACH, fetch = LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {
                @JoinColumn(name = "user_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "role_id")}
    )
    private List<Role> roles;

    public User(Long id) {
        this.id = id;
    }

    public User(CreateUserData data, List<Role> roles) {
        this.name = data.getName();
        this.email = data.getEmail();
        this.password = data.getPassword();
        this.roles = roles;
    }

    public List<String> getRoleInitials() {
        return Optional.ofNullable(this.roles).map(roles -> roles.stream()
                .map(role -> role.getInitials())
                .toList()).orElseGet(() -> List.of());
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    private Boolean getActive() {
        return active;
    }

    public Boolean isActive() {
        return getActive();
    }

    public Boolean validatePassword(String password) {
        return PASSWORD_ENCODER.matches(password, this.password);
    }

    @PrePersist
    private void created() {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
