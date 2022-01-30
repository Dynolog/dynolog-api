package com.github.appointmentsio.api.controllers;

import com.github.appointmentsio.api.domain.session.model.TokenResponse;
import com.github.appointmentsio.api.domain.user.form.CreateUserProps;
import com.github.appointmentsio.api.domain.user.service.CreateUserService;
import static com.github.appointmentsio.api.utils.Response.created;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        @RequestHeader(value = "Accept-Language", required = false) String locale,
        @Valid @RequestBody CreateUserProps body
    ) {
        var token = createService.createWithSession(body);
        return created(token, "api/users", token.getUser().getId());
    }
}
