package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.form.UpdateProjectProps;
import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class UpdateProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public UpdateProjectService(
        ProjectRepository repository
    ) {
        this.projectRepository = repository;
    }

    public ProjectInfo update(String nanoid, UpdateProjectProps props) {

        var authorized = authorizedOrThrow();

        var project = projectRepository.findOptionalByIdFetchUser(nanoid.getBytes(UTF_8))
                .orElseThrow(() -> notFound("Project not found"));

        if (!authorized.canModify(project.getUser())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_MODIFY, "'projects'"));
        }

        project.update(props);

        projectRepository.save(project);

        return new ProjectInfo(project);
    }
}
