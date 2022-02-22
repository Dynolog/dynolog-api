package com.github.appointmentsio.api.domain.project.service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorized;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.user.service.FindUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProjectService {

    private final ProjectRepository projectRepository;
    private final FindUserService findUserService;

    @Autowired
    public CreateProjectService(
            ProjectRepository projectRepository,
            FindUserService findService
    ) {
        this.projectRepository = projectRepository;
        this.findUserService = findService;
    }

    public Project create(CreateProjectProps props) {
        authorized().ifPresent(authorized -> {
            if (!authorized.canModify(props.getUserId())) {
                throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'projects'"));
            }
        });

        var user = findUserService.findOptionalByNanoidFetchRoles(props.getUserId())
                .orElseThrow(() -> notFound("User not found"));

        var project = projectRepository.save(new Project(props, user));

        project.setUser(user);

        return project;
    }
}
