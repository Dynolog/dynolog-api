package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.project.repository.ProjectRepository;
import com.github.throyer.appointments.domain.timeentry.entity.TimeEntry;
import com.github.throyer.appointments.domain.timeentry.model.CreateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.model.SimplifiedTimeEntry;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryInfo;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import com.github.throyer.appointments.domain.user.repository.UserRepository;
import com.github.throyer.appointments.errors.Error;
import com.github.throyer.appointments.errors.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.appointments.utils.Response.notFound;

@Service
public class CreateTimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public CreateTimeEntryService(
        TimeEntryRepository timeEntryRepository,
        UserRepository userRepository,
        ProjectRepository projectRepository
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public TimeEntryInfo create(CreateTimeEntryProps props) {
        var exception = new BadRequestException();

        var start = props.getStart();
        var stop = props.getStop();

        if (start.isAfter(stop) || stop.isBefore(start)) {
            exception.add(new Error("start or stop", "start or stop dates interval is invalid"));
        }

        if (exception.hasError()) {
            throw exception;
        }

        if (!userRepository.existsById(props.getUserId())) {
            throw notFound("User not found");
        }

        props.getProjectId().ifPresent(projectId -> {
            if (!projectRepository.existsById(projectId)) {
                throw notFound("Project not found");
            }
        });

        var id = timeEntryRepository.save(new TimeEntry(props)).getId();
        var timeEntry = timeEntryRepository.findByIdFetchUserAndProject(id);

        return new TimeEntryInfo(timeEntry);
    }
}
