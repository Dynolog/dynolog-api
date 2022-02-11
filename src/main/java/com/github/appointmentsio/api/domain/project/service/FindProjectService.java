package com.github.appointmentsio.api.domain.project.service;

import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FindProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public FindProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectInfo> findAll(String userNanoid) {
        var optional = userRepository.findOptionalIdByNanoid(userNanoid.getBytes(UTF_8));
        return optional.map(this::findAll).orElseGet(List::of);
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
