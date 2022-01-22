package com.github.throyer.appointments.domain.project.service;

import com.github.throyer.appointments.domain.project.model.ProjectDetails;
import com.github.throyer.appointments.domain.project.model.UpdateProjectProps;
import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.throyer.appointments.utils.Response.notFound;
import static com.github.throyer.appointments.utils.Response.unauthorized;

@Service
public class UpdateProjectService {

    private final ProjectRepository repository;

    @Autowired
    public UpdateProjectService(
        ProjectRepository repository
    ) {
        this.repository = repository;
    }

    public ProjectDetails update(Long id, UpdateProjectProps props) {

        var authorized = authorizedOrThrow();

        var project = repository.findByIdFetchUser(id)
                .orElseThrow(() -> notFound("Project not found"));

        if (!authorized.canModify(project.getUser().getId())) {
            throw unauthorized("Not authorized to update projects for this user");
        }

        project.update(props);

        repository.save(project);

        return new ProjectDetails(project);
    }
}
