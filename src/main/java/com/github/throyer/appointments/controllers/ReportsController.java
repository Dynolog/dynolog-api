package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.domain.timeentry.model.Summary;
import com.github.throyer.appointments.domain.timeentry.service.FindTotalInHoursService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@Tag(name = "Reports")
@RequestMapping("api/reports")
@SecurityRequirement(name = "token")
public class ReportsController {

    @Autowired
    private FindTotalInHoursService service;

    @GetMapping("/summary")
    @Operation(summary = "Returns a summary of all time entries for a user")
    public Summary summary(
        @RequestParam("start_date")
        @DateTimeFormat(iso = DATE_TIME)
        LocalDateTime start,
        @RequestParam("end_date")
        @DateTimeFormat(iso = DATE_TIME)
        LocalDateTime end,
        @RequestParam("user_id") Long userID
    ) {
        return service.findTotal(start, end, userID);
    }
}
