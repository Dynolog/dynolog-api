package com.github.appointmentsio.api.domain.session.service;

import static com.github.appointmentsio.api.utils.Constants.SECURITY.ROLES_KEY_ON_JWT;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.lang.String.join;
import static java.util.Arrays.stream;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.github.appointmentsio.api.domain.role.entity.Role;
import com.github.appointmentsio.api.domain.session.model.Authorized;
import com.github.appointmentsio.api.domain.user.entity.User;
import com.github.appointmentsio.api.domain.user.model.SimplifiedUser;

import io.jsonwebtoken.Jwts;

public class JsonWebToken {

    public String encode(User user, LocalDateTime expiration, String secret) {
        var roles = user.getRoles();
        return encode(user.getNanoId(), roles, expiration, secret);
    }
    
    public String encode(SimplifiedUser user, LocalDateTime expiration, String secret) {
        return encode(user.getId(), user.getRoles(), expiration, secret);
    }

    public String encode(
        String id,
        Collection<String> authorities,
        LocalDateTime expiration,
        String secret
    ) {
        return Jwts.builder()
            .setSubject(id)
            .claim(ROLES_KEY_ON_JWT, join(",", authorities))
            .setExpiration(Date.from(expiration
                .atZone(ZoneId.systemDefault())
                .toInstant()))
            .signWith(HS256, secret)
            .compact();
    }

    public Authorized decode(String token, String secret) {
        var decoded = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

        var id = decoded.getBody().getSubject();
        
        var joinedRolesString = decoded.getBody().get(ROLES_KEY_ON_JWT).toString();
        var roles = joinedRolesString.split(",");
        var authorities = stream(roles).map(Role::new).toList();
        return new Authorized(id, authorities);
    }
}
