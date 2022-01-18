package com.github.throyer.appointments.domain.timeentry.entity;

import com.github.throyer.appointments.domain.timeentry.model.CreateTimeEntryProps;
import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.timeentry.model.UpdateTimeEntryProps;
import com.github.throyer.appointments.domain.user.entity.User;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.DETACH;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class TimeEntry implements Serializable {

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

    public TimeEntry(CreateTimeEntryProps props) {
        this.description = props.getDescription();
        this.start = props.getStart();
        this.stop = props.getStop();
        
        this.user = props.getUserId()
                .map(User::new)
                    .orElseGet(() -> null);
        
        this.project = props.getProjectId()
               .map(Project::new)
                   .orElseGet(() -> null);
    }

    public void update(UpdateTimeEntryProps props) {
        this.start = props.getStart();
        this.stop = props.getStop();
        this.description = props.getDescription();

        this.project = props.getProjectId()
                .map(Project::new)
                .orElseGet(() -> null);
    }

    public Optional<User> getUser() {
        return ofNullable(user);
    }

    public Optional<Project> getProject() {
        return ofNullable(project);
    }    
}
