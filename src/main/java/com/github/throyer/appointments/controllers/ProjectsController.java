package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.domain.project.model.CreateProjectProps;
import com.github.throyer.appointments.domain.project.model.ProjectDetails;
import com.github.throyer.appointments.domain.project.model.UpdateProjectProps;
import com.github.throyer.appointments.domain.project.service.CreateProjectService;
import com.github.throyer.appointments.domain.project.service.UpdateProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.throyer.appointments.utils.Response.ok;

@RestController
@Tag(name = "Projects")
@RequestMapping("/api/projects")
public class ProjectsController {

    private final CreateProjectService createService;
    private final UpdateProjectService updateService;

    @Autowired
    public ProjectsController(
        CreateProjectService createService,
        UpdateProjectService updateService
    ) {
        this.createService = createService;
        this.updateService = updateService;
    }

    @PostMapping
    @Operation(summary = "Register a new project")
    public ResponseEntity<ProjectDetails> create(@RequestBody CreateProjectProps body) {
        var project = createService.create(body);
        return ok(project);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a project")
    public ResponseEntity<ProjectDetails> update(@PathVariable Long id, @RequestBody UpdateProjectProps body) {
        var project = updateService.update(id, body);
        return ok(project);
    }
}
