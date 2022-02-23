package com.github.appointmentsio.api.domain.project.service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public FindProjectService(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll(String userNanoId) {
        if (!authorizedOrThrow().canRead(userNanoId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_LIST, "'projects'"));
        }
        return projectRepository.findAllByUserNanoIdFetchUser(userNanoId.getBytes(UTF_8));
    }
}
