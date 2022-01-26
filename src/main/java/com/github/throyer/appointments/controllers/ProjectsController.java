package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.domain.project.model.CreateProjectProps;
import com.github.throyer.appointments.domain.project.model.ProjectInfo;
import com.github.throyer.appointments.domain.project.model.SimplifiedProject;
import com.github.throyer.appointments.domain.project.model.UpdateProjectProps;
import com.github.throyer.appointments.domain.project.service.CreateProjectService;
import com.github.throyer.appointments.domain.project.service.FindProjectService;
import com.github.throyer.appointments.domain.project.service.UpdateProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.throyer.appointments.utils.Response.ok;

@RestController
@Tag(name = "Projects")
@RequestMapping("/api/projects")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('USER')")
public class ProjectsController {

    private final FindProjectService findService;
    private final CreateProjectService createService;
    private final UpdateProjectService updateService;

    @Autowired
    public ProjectsController(
        FindProjectService findService,
        CreateProjectService createService,
        UpdateProjectService updateService
    ) {
        this.findService = findService;
        this.createService = createService;
        this.updateService = updateService;
    }

    @GetMapping
    @Operation(summary = "List all projects from user")
    public ResponseEntity<List<ProjectInfo>> index(
        @RequestParam(value = "user_id") Long userId
    ) {
        var projects = findService.findAll(userId);
        return ok(projects);
    }

    @PostMapping
    @Operation(summary = "Register a new project")
    public ResponseEntity<ProjectInfo> create(
        @RequestBody @Valid CreateProjectProps body
    ) {
        var project = createService.create(body);
        return ok(project);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a project")
    public ResponseEntity<ProjectInfo> update(
        @PathVariable Long id,
        @RequestBody @Valid UpdateProjectProps body
    ) {
        var project = updateService.update(id, body);
        return ok(project);
    }
}
