package com.github.appointmentsio.api.domain.project.service;

import static com.github.appointmentsio.api.utils.Response.notFound;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.form.UpdateProjectProps;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public UpdateProjectService(
        ProjectRepository repository
    ) {
        this.projectRepository = repository;
    }

    public Project update(String id, UpdateProjectProps props) {
        var project = projectRepository.findOptionalByNanoIdFetchUser(id.getBytes(UTF_8))
                .orElseThrow(() -> notFound("Project not found"));

        project.update(props);

        projectRepository.save(project);

        return project;
    }
}
