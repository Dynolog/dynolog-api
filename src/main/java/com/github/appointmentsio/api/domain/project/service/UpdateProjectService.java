package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.form.UpdateProjectProps;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;

@Service
public class UpdateProjectService {

    private final ProjectRepository repository;

    @Autowired
    public UpdateProjectService(
        ProjectRepository repository
    ) {
        this.repository = repository;
    }

    public ProjectInfo update(Long id, UpdateProjectProps props) {

        var authorized = authorizedOrThrow();

        var project = repository.findOptionalByIdFetchUser(id)
                .orElseThrow(() -> notFound("Project not found"));

        if (!authorized.canModify(project.getUser())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_MODIFY, "'projects'"));
        }

        project.update(props);

        repository.save(project);

        var updated = repository.findByIdFetchUser(id);

        return new ProjectInfo(updated);
    }
}