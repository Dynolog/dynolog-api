package com.github.appointmentsio.api.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.TimeZone.getAvailableIDs;


@RestController
@Tag(name = "Timezones")
@RequestMapping("api/timezones")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class TimezonesController {

    @GetMapping
    public List<String> index() {
        return List.of(getAvailableIDs());
    }
}