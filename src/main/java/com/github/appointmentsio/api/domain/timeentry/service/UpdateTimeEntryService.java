package com.github.appointmentsio.api.domain.timeentry.service;

import com.github.appointmentsio.api.domain.project.repository.ProjectRepository;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.form.UpdateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.repository.TimeEntryRepository;
import com.github.appointmentsio.api.errors.Error;
import com.github.appointmentsio.api.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.TIMEENTRY_DATE_INTERVAL_INVALID;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.*;

@Service
public class UpdateTimeEntryService {

    private final  TimeEntryRepository timeEntryRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public UpdateTimeEntryService(
        TimeEntryRepository timeEntryRepository,
        ProjectRepository projectRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.projectRepository = projectRepository;
    }

    public TimeEntryInfo update(Long id, UpdateTimeEntryProps props) {

        var exception = new BadRequestException();

        var start = props.getStart();
        var stop = props.getStop();

        if (start.isAfter(stop) || stop.isBefore(start)) {
            exception.add(new Error("start or stop", message(TIMEENTRY_DATE_INTERVAL_INVALID)));
        }

        if (exception.hasError()) {
            throw exception;
        }

        props.getProjectId().ifPresent(projectId -> {
            if (!projectRepository.existsById(projectId)) {
                throw notFound("Project not found");
            }
        });

        var entry = timeEntryRepository.findById(id)
            .orElseThrow(() -> notFound("time entry not found"));

        var authorized = authorizedOrThrow();

        if (!authorized.canRead(entry.getUser())) {
            throw unauthorized("No editing permission for this time entry");
        }

        entry.update(props);
        timeEntryRepository.save(entry);

        return new TimeEntryInfo(timeEntryRepository.findByIdFetchUserAndProject(id));
    }
}
