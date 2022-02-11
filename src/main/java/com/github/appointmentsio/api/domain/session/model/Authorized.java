package com.github.appointmentsio.api.domain.session.model;

import com.github.appointmentsio.api.domain.role.entity.Role;
import com.github.appointmentsio.api.domain.shared.model.Identity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.appointmentsio.api.utils.JsonUtils.toJson;
import static java.util.stream.Collectors.joining;

public class Authorized extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;

    public Authorized(Long id, List<Role> authorities) {
        super("USERNAME", "SECRET", authorities);
        this.id = id;
        this.name = "";
    }

    public Authorized(com.github.appointmentsio.api.domain.user.entity.User user) {
        super(
            user.getEmail(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            user.getAuthorities()
        );
        this.id = user.getId();
        this.name = user.getName();
    }

    public Long getId() {
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

    public <T extends Identity> Boolean canModify(Optional<T> identity) {
        if (identity.isEmpty()) {
            return false;
        }
        return canRead(identity.get());
    }

    public Boolean canModify(Identity identity) {
        return canRead(identity.getId());
    }

    public Boolean canModify(Long id) {
        var admin = isAdmin();
        var equals = getId().equals(id);
        if (admin) {
            return true;
        }
        return equals;
    }

    public <T extends Identity> Boolean canRead(Optional<T> identity) {
        if (identity.isEmpty()) {
            return false;
        }
        return canRead(identity.get());
    }

    public Boolean canRead(Identity identity) {
        return canRead(identity.getId());
    }

    public Boolean canRead(Long id) {
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
        return toJson(Map.of("id", this.id, "roles", roles));
    }
}
