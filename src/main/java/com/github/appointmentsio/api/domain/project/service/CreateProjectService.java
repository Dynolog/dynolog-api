package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.user.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;

@Service
public class CreateProjectService {

    private final ProjectRepository projectRepository;
    private final FindUserService findService;

    @Autowired
    public CreateProjectService(
        ProjectRepository projectRepository,
        FindUserService findService
    ) {
        this.projectRepository = projectRepository;
        this.findService = findService;
    }

    public ProjectInfo create(CreateProjectProps props) {
        var authorized = authorizedOrThrow();

        if (!authorized.canModify(props.getUserId())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'projects'"));
        }

        var user = findService.findById(props.getUserId())
            .orElseThrow(() -> notFound("User not found"));

        var project = projectRepository.save(new Project(props));

        project.setUser(user);

        return new ProjectInfo(project);
    }
}
