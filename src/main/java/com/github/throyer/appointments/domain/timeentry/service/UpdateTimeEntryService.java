package com.github.throyer.appointments.domain.timeentry.service;

import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.model.UpdateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.repository.TimeEntryRepository;
import com.github.throyer.appointments.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.appointments.domain.session.service.SessionService.authorized;
import static com.github.throyer.appointments.utils.Response.*;

@Service
public class UpdateTimeEntryService {

    private final  TimeEntryRepository repository;

    @Autowired
    public UpdateTimeEntryService(TimeEntryRepository repository) {
        this.repository = repository;
    }


    public TimeEntryDetails update(Long id, UpdateTimeEntryProps props) {
        var entry = repository.findById(id)
                .orElseThrow(() -> notFound("time entry not found"));

        authorized()
            .filter(authorized -> authorized.canModify(entry.getUser().map(User::getId).orElseThrow(() -> internalServerError("Unexpected error loading time-entry"))))
                .orElseThrow(() -> unauthorized("No editing permission for this resource"));

        entry.update(props);
        repository.save(entry);

        return repository.findByIdFetchUserAndProject(id);
    }
}
