package com.github.throyer.apontamentos.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@NoArgsConstructor
public class Role /*implements GrantedAuthority*/ {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String initials;

    private String description;

//    @Override
//    public String getAuthority() {
//        return getInitials();
//    }
}