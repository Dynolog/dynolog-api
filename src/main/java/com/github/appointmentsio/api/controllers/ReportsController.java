package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.timeentry.model.Summary;
import com.github.appointmentsio.api.domain.timeentry.service.PdfService;
import com.github.appointmentsio.api.domain.timeentry.service.findSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.github.appointmentsio.api.utils.Response.ok;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

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

    @GetMapping(produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> index(
            @RequestParam("start_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
            @RequestParam("end_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime end,
            @RequestParam("user_id") String userNanoid
    ) {
        var bytes = pdfService.create(start, end, userNanoid);

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
            @RequestParam("start_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
            @RequestParam("end_date") @DateTimeFormat(iso = DATE_TIME) LocalDateTime end,
            @RequestParam("user_id") String userNanoid
    ) {
        return ok(findSummaryService.findSummaryByUserId(start, end, userNanoid));
    }
}
