package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;

@Service
public class FindProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public FindProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectInfo> findAll(Long userId) {
        if (!authorizedOrThrow().canRead(userId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_LIST, "'projects'"));
        }

        return projectRepository.findAllFetchUser(userId).stream()
                .map(ProjectInfo::new)
                    .toList();
    }
}
