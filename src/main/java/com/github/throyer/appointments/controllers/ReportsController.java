package com.github.throyer.appointments.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reports")
@SecurityRequirement(name = "token")
public class ReportsController {
    
    @GetMapping("/summary")
    public String summary() {
        return "summary has not been developed yet";
    }
}
