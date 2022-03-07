package com.github.dynolog.api.domain.project.service;

import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.notFound;
import static com.github.dynolog.api.utils.Response.unauthorized;

import com.github.dynolog.api.domain.project.entity.Project;
import com.github.dynolog.api.domain.project.form.CreateProjectProps;
import com.github.dynolog.api.domain.project.repository.ProjectRepository;
import com.github.dynolog.api.domain.user.service.FindUserService;

import com.github.dynolog.api.domain.session.service.SessionService;
import com.github.dynolog.api.utils.Response;
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
        SessionService.authorized().ifPresent(authorized -> {
            if (!authorized.canModify(props.getUserId())) {
                throw Response.unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'projects'"));
            }
        });

        var user = findUserService.findOptionalByNanoidFetchRoles(props.getUserId())
                .orElseThrow(() -> Response.notFound("User not found"));

        var project = projectRepository.save(new Project(props, user));

        project.setUser(user);

        return project;
    }
}
