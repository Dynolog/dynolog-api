package com.github.dynolog.api.domain.timeentry.service;

import static com.github.dynolog.api.utils.Constants.MESSAGES.TIMEENTRY_DATE_INTERVAL_INVALID;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.notFound;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

import com.github.dynolog.api.domain.project.entity.Project;
import com.github.dynolog.api.domain.project.repository.ProjectRepository;
import com.github.dynolog.api.domain.timeentry.entity.TimeEntry;
import com.github.dynolog.api.domain.timeentry.form.CreateTimeEntryProps;
import com.github.dynolog.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.dynolog.api.domain.user.service.FindUserService;
import com.github.dynolog.api.errors.exception.BadRequestException;
import com.github.dynolog.api.errors.model.FieldError;

import com.github.dynolog.api.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final ProjectRepository projectRepository;
    private final FindUserService findUserService;

    @Autowired
    public CreateTimeEntryService(
            TimeEntryRepository timeEntryRepository,
            ProjectRepository projectRepository,
            FindUserService findUserService
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.projectRepository = projectRepository;
        this.findUserService = findUserService;
    }

    public TimeEntry create(CreateTimeEntryProps props) {
        var exception = new BadRequestException();

        var start = props.getStart();
        var stop = props.getStop();

        if (nonNull(start) && nonNull(stop) && (start.isAfter(stop) || stop.isBefore(start))) {
            exception.add(new FieldError("start or stop", message(TIMEENTRY_DATE_INTERVAL_INVALID)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        var user = findUserService.findOptionalByNanoidFetchRoles(props.getUserId())
                .orElseThrow(() -> Response.notFound("User not found"));

        Project project = null;

        if (nonNull(props.getProjectId())) {
            var projectNanoId = props.getProjectId();
            project = projectRepository.findOptionalByNanoIdFetchUser(projectNanoId.getBytes(UTF_8))
                    .orElseThrow(() -> Response.notFound("project not found"));
        }

        var created = timeEntryRepository.save(new TimeEntry(props, user, project));

        return timeEntryRepository.findByNanoId(created.getNanoId())
                .orElseThrow();
    }
}
