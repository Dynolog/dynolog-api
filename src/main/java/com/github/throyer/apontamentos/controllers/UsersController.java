package com.github.throyer.apontamentos.controllers;

import com.github.throyer.apontamentos.domain.user.entity.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('ADM')")
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository repository;
}
