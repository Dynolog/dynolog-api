package com.github.throyer.appointments.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Reports")
@RequestMapping("api/reports")
@SecurityRequirement(name = "token")
public class ReportsController {
    
    @GetMapping("/summary")
    @Operation(summary = "Returns a summary of all time entries for a user")
    public String summary() {
        return "summary has not been developed yet";
    }
}
