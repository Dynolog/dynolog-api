package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.timeentry.entity.TimeEntry;
import com.github.appointmentsio.api.domain.timeentry.form.CreateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.domain.user.service.FindUserService;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TIMEENTRY_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

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

    public TimeEntryInfo create(CreateTimeEntryProps props) {

        var exception = new BadRequestException();

        var start = props.getStart();
        var stop = props.getStop();

        if (start.isAfter(stop) || stop.isBefore(start)) {
            exception.add(new Error("start or stop", message(TIMEENTRY_DATE_INTERVAL_INVALID)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        var user = findUserService.findOptionalByNanoidFetchRoles(props.getUserId())
                .orElseThrow(() -> notFound("User not found"));

        var authorized = authorizedOrThrow();

        if (!authorized.canModify(user)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'time entries'"));
        }

        Project project = null;

        if (nonNull(props.getProjectId())) {
            var projectNanoid = props.getProjectId();
            project = projectRepository.findOptionalByIdFetchUser(projectNanoid.getBytes(UTF_8))
                    .orElseThrow(() -> notFound("project not found"));
        }

        var created = timeEntryRepository.save(new TimeEntry(props, user, project));

        var timeEntry = timeEntryRepository.findByIdFetchUserAndProject(created.getId());

        return new TimeEntryInfo(timeEntry);
    }
}
