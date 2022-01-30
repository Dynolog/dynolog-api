package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.service.findSummaryService;
import com.github.appointmentsio.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorized;
import static com.github.appointmentsio.api.utils.Response.ok;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@Tag(name = "Reports")
@RequestMapping("api/reports")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class ReportsController {

    @Autowired
    public ReportsController(findSummaryService service) {
        this.service = service;
    }

    private final findSummaryService service;

    @GetMapping("/summary")
    @Operation(summary = "Returns a summary of all time entries for a user")
    public ResponseEntity<Summary> summary(
        @RequestParam("start_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
        @RequestParam("end_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime end,
        @RequestParam("user_id") Long userId
    ) {
        return ok(service.findSummaryByUserId(start, end, userId));
    }
}