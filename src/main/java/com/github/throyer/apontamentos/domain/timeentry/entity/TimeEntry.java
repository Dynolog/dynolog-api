package com.github.throyer.apontamentos.domain.timeentry.entity;

import com.github.throyer.apontamentos.domain.timeentry.dto.CreateTimeEntryData;
import com.github.throyer.apontamentos.domain.project.entity.Project;
import com.github.throyer.apontamentos.domain.user.entity.User;
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

    public TimeEntry(CreateTimeEntryData data) {
        this.description = data.getDescription();
        this.start = data.getStart();
        this.stop = data.getStop();
        
        this.user = data.getUserId()
                .map(id -> new User(id))
                    .orElseGet(() -> null);
        
        this.project = data.getProjectId()
               .map(id -> new Project(id))
                   .orElseGet(() -> null);
    }

    public Optional<User> getUser() {
        return ofNullable(user);
    }

    public Optional<Project> getProject() {
        return ofNullable(project);
    }    
}
