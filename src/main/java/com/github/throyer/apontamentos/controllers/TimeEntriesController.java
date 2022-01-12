package com.github.throyer.apontamentos.controllers;

import com.github.throyer.apontamentos.domain.timeentry.entity.TimeEntry;
import com.github.throyer.apontamentos.domain.pagination.Page;
import com.github.throyer.apontamentos.domain.timeentry.dto.CreateTimeEntryData;
import com.github.throyer.apontamentos.domain.timeentry.dto.TimeEntryDetails;
import com.github.throyer.apontamentos.domain.timeentry.service.CreateTimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/time_entries")
public class TimeEntriesController {
    
    @Autowired
    private CreateTimeEntryService createService;
    
    public Page<TimeEntry> index() {
        return null;
    }
    
    public TimeEntryDetails create(CreateTimeEntryData data) {
        var created = createService.create(data);
        return created;
    }
}
