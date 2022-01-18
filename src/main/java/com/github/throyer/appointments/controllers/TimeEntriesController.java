package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.domain.pagination.Page;
import com.github.throyer.appointments.domain.timeentry.model.CreateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.model.TimeEntryDetails;
import com.github.throyer.appointments.domain.timeentry.model.UpdateTimeEntryProps;
import com.github.throyer.appointments.domain.timeentry.service.CreateTimeEntryService;
import com.github.throyer.appointments.domain.timeentry.service.FindTimeEntryService;
import com.github.throyer.appointments.domain.timeentry.service.UpdateTimeEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.github.throyer.appointments.utils.Response.created;
import static com.github.throyer.appointments.utils.Response.ok;

@RestController
@SecurityRequirement(name = "token")
@RequestMapping("api/time-entries")
@Tag(name = "Time entries")
public class TimeEntriesController {

    private final CreateTimeEntryService createService;
    private final FindTimeEntryService findService;
    private final UpdateTimeEntryService updateService;

    @Autowired
    public TimeEntriesController(
        CreateTimeEntryService createService,
        FindTimeEntryService findService,
        UpdateTimeEntryService updateService
    ) {
        this.createService = createService;
        this.findService = findService;
        this.updateService = updateService;
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of time entries")
    public ResponseEntity<Page<TimeEntryDetails>> index(
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size,
        @RequestParam("user_id") Optional<Long> userId
    ) {
        var result = findService.findAll(page, size, userId);
        return ok(result);
    }
    
    @PostMapping
    @Operation(summary = "Register a new time entry")
    public ResponseEntity<TimeEntryDetails> create(@RequestBody CreateTimeEntryProps body) {
        var timeEntry = createService.create(body);
        return created(timeEntry, "api/time_entries");
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a time entry")
    public ResponseEntity<TimeEntryDetails> update(@PathVariable Long id, @RequestBody UpdateTimeEntryProps body) {
        var timeEntry = updateService.update(id, body);
        return ok(timeEntry);
    }
}
