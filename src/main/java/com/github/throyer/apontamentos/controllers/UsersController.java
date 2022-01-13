package com.github.throyer.apontamentos.controllers;

import com.github.throyer.apontamentos.domain.session.dto.TokenResponse;
import com.github.throyer.apontamentos.domain.user.dto.CreateUserData;
import com.github.throyer.apontamentos.domain.user.service.CreateUserService;
import static com.github.throyer.apontamentos.utils.Response.created;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UsersController {

    @Autowired
    private CreateUserService createService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<TokenResponse> save(
        @Validated @RequestBody CreateUserData body
    ) {
        var token = createService.createWithSession(body);
        return created(token, "api/users", token.getUser().getId());
    }
}
