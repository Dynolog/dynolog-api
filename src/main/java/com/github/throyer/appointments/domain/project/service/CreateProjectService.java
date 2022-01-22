package com.github.throyer.appointments.domain.project.service;

import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.project.model.CreateProjectProps;
import com.github.throyer.appointments.domain.project.model.ProjectDetails;
import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.throyer.appointments.utils.Response.unauthorized;

@Service
public class CreateProjectService {

    private final ProjectRepository repository;

    @Autowired
    public CreateProjectService(
        ProjectRepository repository
    ) {
        this.repository = repository;
    }

    public ProjectDetails create(CreateProjectProps props) {

        var authorized = authorizedOrThrow();

        if (!authorized.canModify(props.getUserId())) {
            throw unauthorized("Not authorized to register projects for this user");
        }

        var project = repository.save(new Project(props));

        return new ProjectDetails(project);
    }
}
