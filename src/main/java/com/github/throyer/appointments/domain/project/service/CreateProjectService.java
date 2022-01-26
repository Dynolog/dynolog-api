package com.github.throyer.appointments.domain.project.service;

import com.github.throyer.appointments.domain.project.entity.Project;
import com.github.throyer.appointments.domain.project.model.CreateProjectProps;
import com.github.throyer.appointments.domain.project.model.ProjectInfo;
import com.github.throyer.appointments.domain.project.model.SimplifiedProject;
import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import com.github.throyer.appointments.domain.user.repository.UserRepository;
import com.github.throyer.appointments.domain.user.service.FindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.throyer.appointments.utils.Response.notFound;
import static com.github.throyer.appointments.utils.Response.unauthorized;

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
            throw unauthorized("Not authorized to register projects for this user");
        }

        var user = findService.findById(props.getUserId())
            .orElseThrow(() -> notFound("User not found"));

        var project = projectRepository.save(new Project(props));

        project.setUser(user);

        return new ProjectInfo(project);
    }
}
