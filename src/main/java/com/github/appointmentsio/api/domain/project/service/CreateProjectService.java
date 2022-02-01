package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import com.github.appointmentsio.api.domain.user.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CreateProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FindUserService findService;

    @Autowired
    public CreateProjectService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            FindUserService findService
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.findService = findService;
    }

    public ProjectInfo create(CreateProjectProps props) {
        var authorized = authorizedOrThrow();

        var userId = userRepository.findOptionalIdByNanoid(props.getUserId().getBytes(UTF_8))
                .orElseThrow(() -> notFound("User not found"));

        if (!authorized.canModify(userId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'projects'"));
        }

        var user = findService.findById(userId)
                .orElseThrow(() -> notFound("User not found"));

        var project = projectRepository.save(new Project(props, userId));

        project.setUser(user);

        return new ProjectInfo(project);
    }
}
