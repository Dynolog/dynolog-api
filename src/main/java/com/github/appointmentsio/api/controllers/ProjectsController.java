package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.project.form.CreateProjectProps;
import com.github.appointmentsio.api.domain.project.model.ProjectInfo;
import com.github.appointmentsio.api.domain.project.form.UpdateProjectProps;
import com.github.appointmentsio.api.domain.project.service.CreateProjectService;
import com.github.appointmentsio.api.domain.project.service.FindProjectService;
import com.github.appointmentsio.api.domain.project.service.UpdateProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.appointmentsio.api.domain.session.service.SessionService.authorized;
import static com.github.appointmentsio.api.domain.session.service.SessionService.authorizedOrThrow;
import static com.github.appointmentsio.api.utils.Constraints.MESSAGES.NOT_AUTHORIZED_TO_CREATE;
import static com.github.appointmentsio.api.utils.Messages.message;
import static com.github.appointmentsio.api.utils.Response.ok;
import static com.github.appointmentsio.api.utils.Response.unauthorized;

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
            @RequestParam(value = "userId") String userNanoid
    ) {
        var projects = findService.findAll(userNanoid);
        return ok(projects);
    }

    @PostMapping
    @Operation(summary = "Register a new project")
    public ResponseEntity<ProjectInfo> create(
            @RequestBody @Valid CreateProjectProps body
    ) {
        if (!authorizedOrThrow().canModify(body.getUserId())) {
            throw unauthorized(message(NOT_AUTHORIZED_TO_CREATE, "'projects'"));
        }

        var project = createService.create(body);
        return ok(new ProjectInfo(project));
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a project")
    public ResponseEntity<ProjectInfo> update(
            @PathVariable String id,
            @RequestBody @Valid UpdateProjectProps body
    ) {
        var project = updateService.update(id, body);
        return ok(project);
    }
}
