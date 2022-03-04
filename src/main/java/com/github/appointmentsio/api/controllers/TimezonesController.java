package com.github.appointmentsio.api.controllers;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.List.of;
import static java.util.TimeZone.getAvailableIDs;


@RestController
@Tag(name = "Timezones")
@RequestMapping("api/timezones")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class TimezonesController {
    @GetMapping
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            array = @ArraySchema(schema = @Schema(example = "\"America/Sao_Paulo\""))
        )
    )
    public List<String> index() {
        return of(getAvailableIDs());
    }
}