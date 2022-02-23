package com.github.appointmentsio.api.controllers;

import static com.github.appointmentsio.api.utils.Response.ok;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

import java.time.LocalDateTime;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.service.PdfService;
import com.github.appointmentsio.api.domain.timeentry.service.findSummaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Reports")
@RequestMapping("api/reports")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class ReportsController {

    @Autowired
    public ReportsController(
        findSummaryService service,
        PdfService pdfService
    ) {
        this.findSummaryService = service;
        this.pdfService = pdfService;
    }

    private final findSummaryService findSummaryService;
    private final PdfService pdfService;

    @Operation(hidden = true)
    @GetMapping(produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> index(
        @RequestParam("start_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
        @RequestParam("end_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime end,
        @RequestParam("user_id") String userNanoId
    ) {
        var bytes = pdfService.create(start, end, userNanoId);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(APPLICATION_PDF)
                .body(new InputStreamResource(bytes));
    }

    @GetMapping("/summary")
    @Operation(summary = "Returns a summary of all time entries for a user")
    public ResponseEntity<Summary> summary(
            @RequestParam("startDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
            @RequestParam("endDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime end,
            @RequestParam("userId") String userNanoId
    ) {
        return ok(findSummaryService.findSummaryByUserId(start, end, userNanoId));
    }
}
