package com.github.throyer.appointments.controllers;

import com.github.throyer.appointments.domain.session.dto.TokenResponse;
import com.github.throyer.appointments.domain.user.model.CreateUserProps;
import com.github.throyer.appointments.domain.user.service.CreateUserService;
import static com.github.throyer.appointments.utils.Response.created;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "Users")
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    private CreateUserService createService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new user", description = "Returns the new user along with their JWT session information")
    public ResponseEntity<TokenResponse> save(
        @Valid @RequestBody CreateUserProps body
    ) {
        var token = createService.createWithSession(body);
        return created(token, "api/users", token.getUser().getId());
    }
}
