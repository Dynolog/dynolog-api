package com.github.throyer.appointments.domain.project.service;

import com.github.throyer.appointments.domain.project.model.ProjectInfo;
import com.github.throyer.appointments.domain.project.model.SimplifiedProject;
import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.throyer.appointments.utils.Response.unauthorized;

@Service
public class FindProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public FindProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectInfo> findAll(Long userId) {
        if (!authorizedOrThrow().canRead(userId)) {
            throw unauthorized("Not authorized to list this user's projects");
        }

        return projectRepository.findAllFetchUser(userId).stream()
                .map(ProjectInfo::new)
                    .toList();
    }
}
