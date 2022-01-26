package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import com.github.throyer.appointments.domain.timeentry.model.SimplifiedTimeEntry;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryInfo;
import com.github.throyer.appointments.domain.timeentry.model.UpdateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import com.github.throyer.appointments.domain.user.entity.User;
import com.github.throyer.appointments.errors.Error;
import com.github.throyer.appointments.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorized;
import static com.github.throyer.appointments.utils.Response.*;

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
            exception.add(new Error("start or stop", "start or stop dates interval is invalid"));
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

        authorized()
            .filter(authorized -> authorized.canModify(entry.getUser().map(User::getId)
                    .orElseThrow(() -> internalServerError("Unexpected error loading time-entry"))))
                .orElseThrow(() -> unauthorized("No editing permission for this resource"));

        entry.update(props);
        timeEntryRepository.save(entry);

        return new TimeEntryInfo(timeEntryRepository.findByIdFetchUserAndProject(id));
    }
}
