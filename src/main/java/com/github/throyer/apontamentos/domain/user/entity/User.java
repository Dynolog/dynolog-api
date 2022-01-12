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
import com.github.throyer.apontamentos.models.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String docuemnt;

    @JsonProperty(access = WRITE_ONLY)
    private String password;

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
}
