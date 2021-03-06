package com.github.dynolog.api.controllers;

import static com.github.dynolog.api.utils.Response.created;
import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.Valid;

import com.github.dynolog.api.domain.session.model.TokenResponse;
import com.github.dynolog.api.domain.user.form.CreateUserProps;
import com.github.dynolog.api.domain.user.service.CreateUserService;

import com.github.dynolog.api.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users")
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    public UsersController(CreateUserService createService) {
        this.createService = createService;
    }

    private final CreateUserService createService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new user", description = "Returns the new user along with their JWT session information")
    public ResponseEntity<TokenResponse> save(
        @Valid @RequestBody CreateUserProps body
    ) {
        var token = createService.createWithSession(body);
        return Response.created(token, "api/users", token.getUser().getId());
    }
}
