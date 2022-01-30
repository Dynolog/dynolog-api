package com.github.appointmentsio.api.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.appointmentsio.api.domain.role.entity.Role;
import com.github.appointmentsio.api.domain.shared.Addressable;
import com.github.appointmentsio.api.domain.shared.Identity;
import com.github.appointmentsio.api.domain.user.form.CreateUserProps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.github.appointmentsio.api.utils.Constraints.SECURITY.PASSWORD_ENCODER;
import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable, Addressable, Identity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String email;

    @JsonProperty(access = WRITE_ONLY)
    private String password;

    private Boolean active = true;

    private String timezone;
    private String dateFormat;
    private String timeFormat;

    @ManyToMany(cascade = DETACH, fetch = LAZY)
    @JoinTable(
        name = "user_role",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles;

    public User(Long id) {
        this.id = id;
    }

    public User(CreateUserProps props, List<Role> roles) {
        this.name = props.getName();
        this.email = props.getEmail();
        this.password = props.getPassword();
        this.roles = roles;
    }

    public User(Long id, String name, String email, String password, String joinedRoles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = ofNullable(joinedRoles)
            .map(roles -> Arrays.stream(roles.split(",")).map(Role::new).toList())
                .orElse(List.of());
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<String> getRoles() {
        return ofNullable(this.roles).map(roles -> roles.stream()
                .map(Role::getInitials)
                    .toList()).orElseGet(List::of);
    }

    public List<Role> getAuthorities() {
        return this.roles;
    }

    @Override
    public Long getId() {
        return this.id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ofNullable(this.name).orElse("");
    }
}
