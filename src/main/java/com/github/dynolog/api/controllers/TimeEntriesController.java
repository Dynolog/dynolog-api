package com.github.dynolog.api.controllers;

import static com.github.dynolog.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_LIST;
import static com.github.dynolog.api.utils.Constants.MESSAGES.NOT_AUTHORIZED_TO_READ;
import static com.github.dynolog.api.utils.Messages.message;
import static com.github.dynolog.api.utils.Response.created;
import static com.github.dynolog.api.utils.Response.ok;
import static com.github.dynolog.api.utils.Response.unauthorized;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import com.github.dynolog.api.domain.pagination.Page;
import com.github.dynolog.api.domain.timeentry.form.CreateTimeEntryProps;
import com.github.dynolog.api.domain.timeentry.form.UpdateTimeEntryProps;
import com.github.dynolog.api.domain.timeentry.model.StoppedTimeEntry;
import com.github.dynolog.api.domain.timeentry.model.TimeEntryInfo;
import com.github.dynolog.api.domain.timeentry.service.CreateTimeEntryService;
import com.github.dynolog.api.domain.timeentry.service.FindTimeEntriesRunningService;
import com.github.dynolog.api.domain.timeentry.service.FindTimeEntryService;
import com.github.dynolog.api.domain.timeentry.service.UpdateTimeEntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Time entries")
@RequestMapping("api/time-entries")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class TimeEntriesController {

    private final CreateTimeEntryService createService;
    private final FindTimeEntryService findService;
    private final FindTimeEntriesRunningService findRunningService;
    private final UpdateTimeEntryService updateService;

    @Autowired
    public TimeEntriesController(
        CreateTimeEntryService createService,
        FindTimeEntryService findService,
        FindTimeEntriesRunningService findRunningService,
        UpdateTimeEntryService updateService
    ) {
        this.createService = createService;
        this.findService = findService;
        this.findRunningService = findRunningService;
        this.updateService = updateService;
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of time entries")
    public ResponseEntity<Page<StoppedTimeEntry>> index(
        @RequestParam("startDate") @DateTimeFormat(iso = DATE_TIME) Optional<LocalDateTime> start,
        @RequestParam("endDate") @DateTimeFormat(iso = DATE_TIME) Optional<LocalDateTime> end,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size,
        @RequestParam("userId") String userNanoId
    ) {
        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userNanoId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_LIST, "'time entries"));
        }

        var result = findService.findAll(start, end, page, size, userNanoId);
        return ok(result.map(StoppedTimeEntry::new));
    }

    @GetMapping("/running")
    @Operation(summary = "Returns a list of time entries that were not finished")
    public ResponseEntity<TimeEntryInfo> running(@RequestParam("userId") String userNanoId) {
        var authorized = authorizedOrThrow();

        if (!authorized.canRead(userNanoId)) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_READ, "'time-entry'"));
        }

        var result = findRunningService.find(userNanoId);
        return ok(new TimeEntryInfo(result));
    }
    
    @PostMapping
    @Operation(summary = "Register a new time entry")
    public ResponseEntity<TimeEntryInfo> create(
        @RequestBody @Valid CreateTimeEntryProps body
    ) {
        var authorized = authorizedOrThrow();

        if (!authorized.canModify(body.getUserId())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'time entries'"));
        }

        var timeEntry = new TimeEntryInfo(createService.create(body));
        return created(timeEntry, "api/time-entries", timeEntry.getId());
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a time entry")
    public ResponseEntity<TimeEntryInfo> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateTimeEntryProps body
    ) {
        var timeEntry = updateService.update(id, body);
        return ok(new TimeEntryInfo(timeEntry));
    }
}
