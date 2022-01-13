package com.github.throyer.apontamentos.controllers;

import com.github.throyer.apontamentos.domain.shared.pagination.Page;
import com.github.throyer.apontamentos.domain.shared.pagination.Pagination;
import com.github.throyer.apontamentos.domain.timeentry.dto.CreateTimeEntryData;
import com.github.throyer.apontamentos.domain.timeentry.dto.TimeEntryDetails;
import com.github.throyer.apontamentos.domain.timeentry.service.CreateTimeEntryService;
import com.github.throyer.apontamentos.domain.timeentry.service.FindTimeEntryService;

import static com.github.throyer.apontamentos.utils.Response.created;
import static com.github.throyer.apontamentos.utils.Response.ok;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/time_entries")
public class TimeEntriesController {
    
    @Autowired
    private CreateTimeEntryService createService;
    
    @Autowired
    private FindTimeEntryService findService;
    
    @GetMapping
    public ResponseEntity<Page<TimeEntryDetails>> index(
        Pagination pagination,
        @RequestParam("user_id") Optional<Long> userId
    ) {
        var page = findService.findAll(pagination, userId);
        return ok(page);
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateTimeEntryData data) {
        var timeEntry = createService.create(data);
        return created(timeEntry, "api/time_entries");
//          return ok(data);
    }
}
