package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.project.entity.Project;
import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.timeentry.form.UpdateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.ValidationError;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TIMEENTRY_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.notFound;
import static com.github.appointmentsio.api.utils.Response.unauthorized;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

@Service
public class UpdateTimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public UpdateTimeEntryService(
            TimeEntryRepository timeEntryRepository,
            ProjectRepository projectRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.projectRepository = projectRepository;
    }

    public TimeEntryInfo update(String nanoid, UpdateTimeEntryProps props) {

        var exception = new BadRequestException();

        var start = props.getStart();
        var stop = props.getStop();

        if (nonNull(start) && nonNull(stop) && (start.isAfter(stop) || stop.isBefore(start))) {
            exception.add(new ValidationError("start or stop", message(TIMEENTRY_DATE_INTERVAL_INVALID)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        Project project = null;

        if (nonNull(props.getProjectId())) {
            var projectNanoid = props.getProjectId();
            project = projectRepository.findOptionalByIdFetchUser(projectNanoid.getBytes(UTF_8))
                    .orElseThrow(() -> notFound("project not found"));
        }

        var timeEntry = timeEntryRepository
                .findOptionalByNanoidFetchUserAndProject(nanoid.getBytes(UTF_8))
                    .orElseThrow(() -> notFound("time entry not found"));

        var authorized = authorizedOrThrow();

        if (!authorized.canRead(timeEntry.getUser().getNanoid())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_MODIFY, "'time entries'"));
        }

        timeEntry.update(props, project);

        timeEntryRepository.save(timeEntry);

        return new TimeEntryInfo(timeEntry);
    }
}
