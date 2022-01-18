package com.github.throyer.appointments.domain.session.service;

import com.github.throyer.appointments.domain.role.entity.Role;
import com.github.throyer.appointments.domain.session.model.Authorized;
import com.github.throyer.appointments.domain.user.model.UserDetails;
import com.github.throyer.appointments.domain.user.entity.User;

import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import java.time.LocalDateTime;
import static java.util.Arrays.stream;
import java.util.List;

public class JsonWebToken {
    
    public static final String ROLES_KEY_ON_JWT = "roles";

    public String encode(User user, LocalDateTime expiration, String secret) {
        var roles = user.getRoles().stream().map(role -> role.getAuthority()).toList();
        return encode(user.getId(), roles, expiration, secret);
    }
    
    public String encode(UserDetails user, LocalDateTime expiration, String secret) {
        return encode(user.getId(), user.getRoles(), expiration, secret);
    }


    public String encode(
        Long id,
        List<String> authorities,
        LocalDateTime expiration,
        String secret
    ) {
        return Jwts.builder()
            .setSubject(id.toString())
            .claim(ROLES_KEY_ON_JWT, authorities
                .stream()                
                .collect(Collectors.joining(",")))
            .setExpiration(Date.from(expiration
                .atZone(ZoneId.systemDefault())
                .toInstant()))
            .signWith(HS256, secret)
            .compact();
    }

    public Authorized decode(String token, String secret) {

        var decoded = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

        var id = Long.parseLong(decoded.getBody().getSubject());
        
        var joinedRolesString = decoded.getBody().get(ROLES_KEY_ON_JWT).toString();
        var roles = joinedRolesString.split(",");
        var authorities = stream(roles).map(Role::new).toList();

        return new Authorized(id, authorities);
    }
}
