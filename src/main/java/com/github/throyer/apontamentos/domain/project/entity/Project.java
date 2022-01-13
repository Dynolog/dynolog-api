package com.github.throyer.apontamentos.domain.project.entity;

import com.github.throyer.apontamentos.domain.user.entity.User;
import java.io.Serializable;
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
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Project(Long id) {
        this.id = id;
        this.user = null;
    }
}
