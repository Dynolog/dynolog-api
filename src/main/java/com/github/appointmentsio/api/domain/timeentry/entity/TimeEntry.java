package com.github.appointmentsio.api.domain.timeentry.entity;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.shared.model.NonSequentialId;
import com.github.appointmentsio.api.domain.timeentry.form.CreateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.form.UpdateTimeEntryProps;
import com.github.appointmentsio.api.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TimeEntry extends NonSequentialId implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String description;
    private LocalDateTime start;
    private LocalDateTime stop;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "project_id")
    private Project project;

    public TimeEntry(
            Long id,
            byte[] nanoid,
            String description,
            LocalDateTime start,
            LocalDateTime stop,
            Long userId,
            byte[] userNanoid,
            String userName,
            Long projectId,
            byte[] projectNanoid,
            String projectName,
            BigDecimal hourlyHate,
            String currency
    ) {
        this.id = id;
        this.nanoid = nanoid;

        this.description = description;
        this.start = start;
        this.stop = stop;

        if (nonNull(userId)) {
            this.user = new User(userId, userNanoid, userName);
        }

        if (nonNull(projectId)) {
            this.project = new Project(projectId, projectNanoid, projectName, hourlyHate, currency);
        }
    }

    public TimeEntry(CreateTimeEntryProps props, Long userId, Optional<Long> projectId) {
        this.description = props.getDescription();
        this.start = props.getStart();
        this.stop = props.getStop();

        this.user = new User(userId);

        this.project = projectId
                .map(Project::new)
                .orElse(null);
    }

    public void update(UpdateTimeEntryProps props, Optional<Long> projectId) {
        this.start = props.getStart();
        this.stop = props.getStop();
        this.description = props.getDescription();

        this.project = projectId
                .map(Project::new)
                .orElseGet(() -> null);
    }

    public Long totalTimeInMillis() {
        return MILLIS.between(start, stop);
    }

    public Optional<User> getUser() {
        return ofNullable(user);
    }

    public Optional<Project> getProject() {
        return ofNullable(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return id != null && Objects.equals(id, timeEntry.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return format("| %s - %s | %s |", start, stop, description);
    }

    /**
     * Sum total millis from time entries list
     *
     * @param timeEntries
     * @return total millis
     */
    public static Long sum(List<TimeEntry> timeEntries) {
        return timeEntries.stream()
                .map(TimeEntry::totalTimeInMillis)
                .reduce(0L, Long::sum);
    }
}
