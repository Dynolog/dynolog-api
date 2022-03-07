package com.github.dynolog.api.controllers;

import static com.github.dynolog.api.utils.Response.ok;

import javax.validation.Valid;

import com.github.dynolog.api.domain.session.form.CreateRefreshTokenProps;
import com.github.dynolog.api.domain.session.form.CreateTokenProps;
import com.github.dynolog.api.domain.session.model.RefreshTokenResponse;
import com.github.dynolog.api.domain.session.model.TokenResponse;
import com.github.dynolog.api.domain.session.service.CreateTokenService;
import com.github.dynolog.api.domain.session.service.RefreshTokenService;

import com.github.dynolog.api.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/api/sessions")
public class SessionsController {

    @Autowired
    private CreateTokenService createService;

    @Autowired
    private RefreshTokenService refreshService;

    @PostMapping
    @Operation(summary = "Create a jwt token")
    public ResponseEntity<TokenResponse> create(
        @RequestBody @Valid CreateTokenProps request
    ) {
        var token = createService.create(request);
        return Response.ok(token);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Create a new jwt token from refresh code")
    public ResponseEntity<RefreshTokenResponse> refresh(
        @RequestBody @Valid CreateRefreshTokenProps request
    ) {
        var token = refreshService.refresh(request);
        return Response.ok(token);
    }
}
