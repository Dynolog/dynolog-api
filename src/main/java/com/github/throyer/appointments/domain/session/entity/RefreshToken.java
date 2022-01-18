package com.github.throyer.appointments.domain.session.entity;

import com.github.throyer.appointments.domain.user.model.UserDetails;
import com.github.throyer.appointments.domain.user.entity.User;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshToken implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime expiresIn;

    private Boolean available = true;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(User user, Integer daysToExpire) {
        this.user = user;
        this.expiresIn = now().plusDays(daysToExpire);
        this.code = randomUUID().toString();
    }

    public RefreshToken(UserDetails user, Integer daysToExpire) {
        this.expiresIn = now().plusDays(daysToExpire);
        this.code = randomUUID().toString();
        this.user = Optional.ofNullable(user.getId()).map(User::new).orElse(null);
    }

    public Boolean nonExpired() {
        return expiresIn.isAfter(now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RefreshToken that = (RefreshToken) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
