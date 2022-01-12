package com.github.throyer.apontamentos.models;

import com.github.throyer.apontamentos.domain.user.entity.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
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
    
    private User user;

    public Project(Long id) {
        this.id = id;
    }
}
