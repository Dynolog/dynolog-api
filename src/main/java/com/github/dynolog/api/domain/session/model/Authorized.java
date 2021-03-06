package com.github.dynolog.api.domain.session.model;

import static com.github.dynolog.api.utils.JSON.stringify;
import static java.util.stream.Collectors.joining;

import java.io.Serial;
import java.util.List;
import java.util.Map;

import com.github.dynolog.api.domain.role.entity.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class Authorized extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;

    public Authorized(String id, List<Role> authorities) {
        super("USERNAME", "SECRET", authorities);
        this.id = id;
        this.name = "";
    }

    public Authorized(com.github.dynolog.api.domain.user.entity.User user) {
        super(
            user.getEmail(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            user.getAuthorities()
        );
        this.id = user.getNanoId();
        this.name = user.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
    }

    public Boolean isAdmin() {
        return getAuthorities()
            .stream()
                .anyMatch((role) -> role.getAuthority().equals("ADM"));
    }

    public Boolean canModify(String id) {
        var admin = isAdmin();
        var equals = this.id.equals(id);
        if (admin) {
            return true;
        }
        return equals;
    }

    public Boolean canRead(String id) {
        var admin = isAdmin();
        var equals = this.id.equals(id);
        if (admin) {
            return true;
        }
        return equals;
    }

    @Override
    public String toString() {
        var roles = this.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(joining(","));
        return stringify(Map.of("id", this.id, "roles", roles));
    }
}
