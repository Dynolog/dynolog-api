package com.github.dynolog.api.domain.project.service;

import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;

import com.github.dynolog.api.domain.project.entity.Project;
import com.github.dynolog.api.domain.project.repository.ProjectRepository;

import com.github.dynolog.api.domain.session.service.SessionService;
import com.github.dynolog.api.utils.Response;
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
        if (!SessionService.authorizedOrThrow().canRead(userNanoId)) {
            throw Response.unauthorized(message(NOT_AUTHORIZED_TO_LIST, "'projects'"));
        }
        return projectRepository.findAllByUserNanoIdFetchUser(userNanoId.getBytes(UTF_8));
    }
}
