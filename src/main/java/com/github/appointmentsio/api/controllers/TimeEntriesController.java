package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.pagination.Page;
import com.github.appointmentsio.api.domain.timeentry.form.CreateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.model.TimeEntryInfo;
import com.github.appointmentsio.api.domain.timeentry.form.UpdateTimeEntryProps;
import com.github.appointmentsio.api.domain.timeentry.service.CreateTimeEntryService;
import com.github.appointmentsio.api.domain.timeentry.service.FindTimeEntryService;
import com.github.appointmentsio.api.domain.timeentry.service.UpdateTimeEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.appointmentsio.api.utils.Response.created;
import static com.github.appointmentsio.api.utils.Response.ok;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@Tag(name = "Time entries")
@RequestMapping("api/time-entries")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
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
    public ResponseEntity<Page<TimeEntryInfo>> index(
        @RequestParam("start_date") @DateTimeFormat(iso = DATE_TIME) Optional<LocalDateTime> start,
        @RequestParam("end_date") @DateTimeFormat(iso = DATE_TIME) Optional<LocalDateTime> end,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size,
        @RequestParam("user_id") String userNanoid
    ) {
        var result = findService.findAll(start, end, page, size, userNanoid);
        return ok(result);
    }
    
    @PostMapping
    @Operation(summary = "Register a new time entry")
    public ResponseEntity<TimeEntryInfo> create(
        @RequestBody @Valid CreateTimeEntryProps body
    ) {
        var timeEntry = createService.create(body);
        return created(timeEntry, "api/time_entries", timeEntry.getId());
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a time entry")
    public ResponseEntity<TimeEntryInfo> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateTimeEntryProps body
    ) {
        var timeEntry = updateService.update(id, body);
        return ok(timeEntry);
    }
}
