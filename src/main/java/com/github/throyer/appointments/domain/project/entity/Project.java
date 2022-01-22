package com.github.throyer.appointments.domain.project.entity;

import com.github.throyer.appointments.domain.project.model.CreateProjectProps;
import com.github.throyer.appointments.domain.project.model.UpdateProjectProps;
import com.github.throyer.appointments.domain.user.entity.User;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private BigDecimal hourlyHate;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(Long id) {
        this.id = id;
        this.user = null;
    }

    public Project(CreateProjectProps props) {
        this.name = props.getName();
        this.hourlyHate = props.getHourlyHate();
        this.user = new User(props.getUserId());
    }

    public void update(UpdateProjectProps props) {
        this.name = props.getName();
        this.hourlyHate = props.getHourlyHate();
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
