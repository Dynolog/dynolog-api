package com.github.appointmentsio.api.domain.project.entity;

import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.form.UpdateProjectProps;
import com.github.appointmentsio.api.domain.shared.model.NonSequentialId;
import com.github.appointmentsio.api.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static com.github.appointmentsio.api.utils.Constraints.CURRENCY.CURRENCY_SCALE;
import static com.github.appointmentsio.api.utils.Constraints.CURRENCY.HOURS_IN_MILLISECONDS;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Project extends NonSequentialId implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private BigDecimal hourlyRate;
    private String currency;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(Long id) {
        this.id = id;
        this.user = null;
    }

    public Project(CreateProjectProps props, Long userId) {
        this.name = props.getName();
        this.user = new User(userId);
        this.hourlyRate = props.getHourlyRate();
        this.currency = props.getCurrency();
    }

    public Project(Long id, byte[] nanoid, String name, BigDecimal hourlyRate, String currency) {
        this.id = id;
        this.nanoid = nanoid;

        this.name = name;
        this.hourlyRate = hourlyRate;
        this.currency = currency;
    }

    public Project(Long id, byte[] nanoid, String name, BigDecimal hourlyRate, Long userId, byte[] userNanoid, String userName) {
        this.id = id;
        this.nanoid = nanoid;

        this.name = name;
        this.hourlyRate = hourlyRate;
        if (nonNull(userId)) {
            this.user = new User(userId, userNanoid, userName);
        }
    }

    public void update(UpdateProjectProps props) {
        this.name = props.getName();
        this.hourlyRate = props.getHourlyRate();
        this.currency = props.getCurrency();
    }

    public BigDecimal calcBillableValue(Long millis) {
        var hours = millis / HOURS_IN_MILLISECONDS;
        return this.hourlyRate.multiply(valueOf(hours))
            .setScale(CURRENCY_SCALE, HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return id != null && Objects.equals(id, project.id);
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
